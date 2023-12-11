package br.com.rafaelvieira.shopbeer.controller;

import java.util.List;

import br.com.rafaelvieira.shopbeer.controller.page.PageWrapper;
import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.Style;
import br.com.rafaelvieira.shopbeer.domain.dto.BeerDTO;
import br.com.rafaelvieira.shopbeer.domain.enums.Flavor;
import br.com.rafaelvieira.shopbeer.domain.enums.Origin;
import br.com.rafaelvieira.shopbeer.repository.BeerRepository;
import br.com.rafaelvieira.shopbeer.repository.StyleRepository;
import br.com.rafaelvieira.shopbeer.repository.filter.BeerFilter;
import br.com.rafaelvieira.shopbeer.service.BeerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/beers")
public class BeerController {
	
	private final StyleRepository styleRepository;
	private final BeerService beerService;
	private final BeerRepository beerRepository;

	public BeerController(StyleRepository styleRepository, BeerService beerService, BeerRepository beerRepository) {
		this.styleRepository = styleRepository;
		this.beerService = beerService;
		this.beerRepository = beerRepository;
	}

	@RequestMapping("/new")
	public ModelAndView new(Beer beer) {
		ModelAndView mv = new ModelAndView("beer/RegisterBeer");
		mv.addObject("flavors", Flavor.values());
		mv.addObject("style", styleRepository.findAll());
		mv.addObject("origin", Origin.values());
		return mv;
	}
	
	@RequestMapping(value = { "/new", "{\\d+}" }, method = RequestMethod.POST)
	public ModelAndView save(@Valid Beer beer, BindingResult result, Model model, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return new(beer);
		}
		
		beerService.save(beer);
		attributes.addFlashAttribute("mensagem", "Beer salva com sucesso!");
		return new ModelAndView("redirect:/cervejas/nova");
	}
	
	@GetMapping
	public ModelAndView pesquisar(BeerFilter filter, BindingResult result
			, @PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cerveja/PesquisaCervejas");
		mv.addObject("styles", estilos.findAll());
		mv.addObject("flavors", Flavor.values());
		mv.addObject("origins", Origin.values());
		
		PageWrapper<Beer> pageWrapper = new PageWrapper<>(beerRepository.filtered(filter, pageable)
				, httpServletRequest);
		mv.addObject("page", pageWrapper);
		return mv;
	}

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<BeerDTO> search(String skuOrName) {
		return beerRepository.bySkuOrName(skuOrName);
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Beer cerveja) {
		try {
			cadastroCervejaService.excluir(cerveja);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Beer cerveja) {
		ModelAndView mv = nova(cerveja);
		mv.addObject(cerveja);
		return mv;
	}
	
}
