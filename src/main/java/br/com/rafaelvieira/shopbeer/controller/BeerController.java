package br.com.rafaelvieira.shopbeer.controller;

import br.com.rafaelvieira.shopbeer.controller.page.PageWrapper;
import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.dto.BeerDTO;
import br.com.rafaelvieira.shopbeer.domain.enums.Flavor;
import br.com.rafaelvieira.shopbeer.domain.enums.Origin;
import br.com.rafaelvieira.shopbeer.repository.StyleRepository;
import br.com.rafaelvieira.shopbeer.repository.filter.BeerFilter;
import br.com.rafaelvieira.shopbeer.repository.query.beer.BeersQuery;
import br.com.rafaelvieira.shopbeer.service.BeerService;
import br.com.rafaelvieira.shopbeer.service.exception.ImpossibleDeleteEntityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/beers")
public class BeerController {
	
	private final StyleRepository styleRepository;
	private final BeerService beerService;
	private final BeersQuery beersQuery;

	public BeerController(StyleRepository styleRepository, BeerService beerService, BeersQuery beersQuery) {
		this.styleRepository = styleRepository;
		this.beerService = beerService;
        this.beersQuery = beersQuery;
    }

	@RequestMapping("/new")
	public ModelAndView newBeer(Beer beer) {
		ModelAndView mv = new ModelAndView("beer/register-beer");
		mv.addObject("flavors", Flavor.values());
		mv.addObject("styles", styleRepository.findAll());
		mv.addObject("origins", Origin.values());
		return mv;
	}

	@PostMapping({ "/new", "/{d}"})
	public ModelAndView save(@Valid @PathVariable("d") Beer beer, BindingResult result, Model model, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return newBeer(beer);
		}

		beerService.save(beer);
		attributes.addFlashAttribute("message", "Beer successfully saved!");
		return new ModelAndView("redirect:/beers/new");
	}
	
	@GetMapping
	public ModelAndView search(BeerFilter filter, BindingResult result
			, @PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("beer/search-beers");
		mv.addObject("styles", styleRepository.findAll());
		mv.addObject("flavors", Flavor.values());
		mv.addObject("origins", Origin.values());
		
		PageWrapper<Beer> pageWrapper = new PageWrapper<>(beersQuery.filtered(filter, pageable)
				, httpServletRequest);
		mv.addObject("page", pageWrapper);
		return mv;
	}

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<BeerDTO> search(String skuOrName) {
		return beersQuery.bySkuOrName(skuOrName);
	}
	
	@DeleteMapping("/{code}")
	public ResponseEntity<?> delete(@PathVariable("code") Beer beer) {
		try {
			beerService.delete(beer);
		} catch (ImpossibleDeleteEntityException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{code}")
	public ModelAndView edit(@PathVariable("code") Beer beer) {
		ModelAndView mv = newBeer(beer);
		mv.addObject(beer);
		return mv;
	}
	
}
