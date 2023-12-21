package br.com.rafaelvieira.shopbeer.controller;

import java.util.List;

import br.com.rafaelvieira.shopbeer.controller.page.PageWrapper;
import br.com.rafaelvieira.shopbeer.repository.query.costumer.CostumerQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.domain.enums.TypePerson;
import br.com.rafaelvieira.shopbeer.repository.CostumerRepository;
import br.com.rafaelvieira.shopbeer.repository.StateRepository;
import br.com.rafaelvieira.shopbeer.repository.filter.CostumerFilter;
import br.com.rafaelvieira.shopbeer.service.CostumerService;
import br.com.rafaelvieira.shopbeer.service.exception.CpfCnpjCustomerAlreadyRegisteredException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/costumers")
public class CostumerController {

	private final StateRepository stateRepository;
	private final CostumerService costumerService;
	private final CostumerRepository costumerRepository;
	private final CostumerQuery costumerQuery;

    public CostumerController(StateRepository stateRepository, CostumerService costumerService, CostumerRepository costumerRepository, CostumerQuery costumerQuery) {
        this.stateRepository = stateRepository;
        this.costumerService = costumerService;
        this.costumerRepository = costumerRepository;
        this.costumerQuery = costumerQuery;
    }

    @RequestMapping("/new")
	public ModelAndView newCostumer(Costumer costumer) {
		ModelAndView mv = new ModelAndView("costumer/register-costumer");
		mv.addObject("typePerson", TypePerson.values());
		mv.addObject("states", stateRepository.findAll());
		return mv;
	}
	
	@PostMapping("/new")
	public ModelAndView save(@Valid Costumer costumer, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return newCostumer(costumer);
		}
		
		try {
			costumerService.save(costumer);
		} catch (CpfCnpjCustomerAlreadyRegisteredException e) {
			result.rejectValue("cpfOrCnpj", e.getMessage(), e.getMessage());
			return newCostumer(costumer);
		}
		
		attributes.addFlashAttribute("message", "Client successfully saved!");
		return new ModelAndView("redirect:/costumers/new");
	}
	
	@GetMapping
	public ModelAndView search(CostumerFilter filter, BindingResult result
			, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("CostumerSearch");
		mv.addObject("typePerson", TypePerson.values());
		
		PageWrapper<Costumer> paginaWrapper = new PageWrapper<>(costumerQuery.filter(filter, pageable)
				, httpServletRequest);
		mv.addObject("page", paginaWrapper);
		return mv;
	}
	
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public List<Costumer> search(String name) {
		validateSizeName(name);
		return costumerRepository.findByNameStartingWithIgnoreCase(name);
	}

	private void validateSizeName(String name) {
		if (StringUtils.hasText(name) || name.length() < 3) {
			throw new IllegalArgumentException();
		}
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}
	
}
