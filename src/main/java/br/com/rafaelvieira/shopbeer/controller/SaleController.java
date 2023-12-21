package br.com.rafaelvieira.shopbeer.controller;

import br.com.rafaelvieira.shopbeer.controller.page.PageWrapper;
import br.com.rafaelvieira.shopbeer.controller.validator.SaleValidator;
import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.ItemSale;
import br.com.rafaelvieira.shopbeer.domain.Sale;
import br.com.rafaelvieira.shopbeer.domain.dto.OriginSalesDTO;
import br.com.rafaelvieira.shopbeer.domain.dto.SalesMonthDTO;
import br.com.rafaelvieira.shopbeer.domain.enums.StatusSale;
import br.com.rafaelvieira.shopbeer.domain.enums.TypePerson;
import br.com.rafaelvieira.shopbeer.mail.Mailer;
import br.com.rafaelvieira.shopbeer.repository.BeerRepository;
import br.com.rafaelvieira.shopbeer.repository.filter.SaleFilter;
import br.com.rafaelvieira.shopbeer.repository.query.sale.SalesQuery;
import br.com.rafaelvieira.shopbeer.security.UserEmployeeSystem;
import br.com.rafaelvieira.shopbeer.service.SaleService;
import br.com.rafaelvieira.shopbeer.session.TablesItemsSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/sales")
public class SaleController {

	private final BeerRepository beerRepository;
	private final TablesItemsSession tablesItemsSession;
	private final SaleService saleService;
	private final SaleValidator saleValidator;
	private final Mailer mailer;
	private final SalesQuery salesQuery;

    public SaleController(BeerRepository beerRepository, TablesItemsSession tablesItemsSession, SaleService saleService,
                          SaleValidator saleValidator, Mailer mailer, SalesQuery salesQuery) {
        this.beerRepository = beerRepository;
        this.tablesItemsSession = tablesItemsSession;
        this.saleService = saleService;
        this.saleValidator = saleValidator;
        this.mailer = mailer;
        this.salesQuery = salesQuery;
    }

    @GetMapping("/new")
	public ModelAndView newSale(Sale sale) {
		ModelAndView mv = new ModelAndView("sale/register-sale");
		
		setUuid(sale);
		
		mv.addObject("itens", sale.getItens());
		mv.addObject("tValueShipping", sale.getValueShipping());
		mv.addObject("ValueDiscount", sale.getValueDiscount());
		mv.addObject("Amount", tablesItemsSession.getAmount(sale.getUuid()));
		
		return mv;
	}
	
	@PostMapping(value = "/new", params = "save")
	public ModelAndView salvar(Sale sale, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UserEmployeeSystem userEmployeeSystem) {
		validSale(sale, result);
		if (result.hasErrors()) {
			return newSale(sale);
		}
		
		sale.setUserEmployee(userEmployeeSystem.getUserEmployee());
		
		saleService.save(sale);
		attributes.addFlashAttribute("message", "Successfully saved sale");
		return new ModelAndView("redirect:/sales/new");
	}

	@PostMapping(value = "/new", params = "issue")
	public ModelAndView issue(Sale sale, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UserEmployeeSystem userEmployeeSystem) {
		validSale(sale, result);
		if (result.hasErrors()) {
			return newSale(sale);
		}
		
		sale.setUserEmployee(userEmployeeSystem.getUserEmployee());
		
		saleService.issue(sale);
		attributes.addFlashAttribute("message", "Successfully issued sale");
		return new ModelAndView("redirect:/sales/new");
	}
	
	@PostMapping(value = "/new", params = "sendEmail")
	public ModelAndView sendEmail(Sale sale, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UserEmployeeSystem userEmployeeSystem) {
		validSale(sale, result);
		if (result.hasErrors()) {
			return newSale(sale);
		}

		sale.setUserEmployee(userEmployeeSystem.getUserEmployee());

		sale = saleService.save(sale);
		mailer.send(sale);
		
		attributes.addFlashAttribute("message", String.format("Sale nº %d saved successfully and email sent", sale.getCode()));
		return new ModelAndView("redirect:/sales/new");
	}
	
	@PostMapping("/item")
	public ModelAndView addItem(Long codeBeer, String uuid) {
		Beer beer = beerRepository.getReferenceById(codeBeer);
		tablesItemsSession.addItem(uuid, beer, 1);
		return mvTableItensSale(uuid);
	}
	
	@PutMapping("/item/{codeBeer}")
	public ModelAndView changeQuantityItem(@PathVariable("codeBeer") Beer beer
			, Integer quantity, String uuid) {
		tablesItemsSession.changeQuantityItems(uuid, beer, quantity);
		return mvTableItensSale(uuid);
	}
	
	@DeleteMapping("/item/{uuid}/{codeBeer}")
	public ModelAndView deleteItem(@PathVariable("codeBeer") Beer beer
			, @PathVariable String uuid) {
		tablesItemsSession.deleteItem(uuid, beer);
		return mvTableItensSale(uuid);
	}
	
	@GetMapping
	public ModelAndView search(SaleFilter filter,
								  @PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("sale/search-sales");
		mv.addObject("allStatus", StatusSale.values());
		mv.addObject("typePerson", TypePerson.values());
		
		PageWrapper<Sale> paginaWrapper = new PageWrapper<>(salesQuery.filtered(filter, pageable)
				, httpServletRequest);
		mv.addObject("page", paginaWrapper);
		return mv;
	}
	
	@GetMapping("/{code}")
	public ModelAndView edit(@PathVariable Long code) {
		Sale sale = salesQuery.searchWithItems(code);
		
		setUuid(sale);
		for (ItemSale item : sale.getItens()) {
			tablesItemsSession.addItem(sale.getUuid(), item.getBeer(), item.getQuantity());
		}
		
		ModelAndView mv = newSale(sale);
		mv.addObject(sale);
		return mv;
	}
	
	@PostMapping(value = "/new", params = "cancel")
	public ModelAndView cancel(Sale sale, BindingResult result
				, RedirectAttributes attributes, @AuthenticationPrincipal UserEmployeeSystem userEmployeeSystem) {
		try {
			saleService.cancel(sale);
		} catch (AccessDeniedException e) {
			ModelAndView mv = new ModelAndView("error");
			mv.addObject("status", 403);
			return mv;
		}
		
		attributes.addFlashAttribute("message", "Sale successfully canceled");
		return new ModelAndView("redirect:/sales/" + sale.getCode());
	}
	
	@GetMapping("/total-by-month")
	public List<SalesMonthDTO> listarTotalVendaPorMes() {
		return salesQuery.totalByMonth();
	}
	
	@GetMapping("/by-origin")
	public List<OriginSalesDTO> salesByNationality() {
		return this.salesQuery.totalByOrigin();
	}
	
	private ModelAndView mvTableItensSale(String uuid) {
		ModelAndView mv = new ModelAndView("sale/table-itens-sale");
		mv.addObject("itens", tablesItemsSession.getItens(uuid));
		mv.addObject("Amount", tablesItemsSession.getAmount(uuid));
		return mv;
	}
	
	private void validSale(Sale sale, BindingResult result) {
		sale.addItems(tablesItemsSession.getItens(sale.getUuid()));
		sale.calculateTotalValue();
		
		saleValidator.validate(sale, result);
	}
	
	private void setUuid(Sale sale) {
		if (StringUtils.hasText(sale.getUuid())) {
			sale.setUuid(UUID.randomUUID().toString());
		}
	}

}
