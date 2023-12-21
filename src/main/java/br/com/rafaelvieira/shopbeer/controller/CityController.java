package br.com.rafaelvieira.shopbeer.controller;

import br.com.rafaelvieira.shopbeer.controller.page.PageWrapper;
import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.repository.CityRepository;
import br.com.rafaelvieira.shopbeer.repository.StateRepository;
import br.com.rafaelvieira.shopbeer.repository.filter.CityFilter;
import br.com.rafaelvieira.shopbeer.repository.query.city.CitiesQuery;
import br.com.rafaelvieira.shopbeer.service.CityService;
import br.com.rafaelvieira.shopbeer.service.exception.NameCityAlreadyRegisteredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CityController {

	private final CityRepository cityRepository;
	private final StateRepository stateRepository;
	private final CityService cityService;
	private final CitiesQuery citiesQuery;

    public CityController(CityRepository cityRepository, StateRepository stateRepository, CityService cityService, CitiesQuery citiesQuery) {
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.cityService = cityService;
        this.citiesQuery = citiesQuery;
    }

    @RequestMapping("/new")
	public ModelAndView newCity(City city) {
		ModelAndView mv = new ModelAndView("city/register-city");
		mv.addObject("states", stateRepository.findAll());
		return mv;
	}
	
	@Cacheable(value = "cities", key = "#codeState")
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<City> searchByStateCode(
			@RequestParam(name = "state", defaultValue = "-1") Long codeState) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {	}
		return cityRepository.findByStateCode(codeState);
	}
	
	@PostMapping("/new")
	@CacheEvict(value = "cities", key = "#city.state.code", condition = "#city.hasState()")
	public ModelAndView save(@Valid City city, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return newCity(city);
		}
		
		try {
			cityService.save(city);
		} catch (NameCityAlreadyRegisteredException e) {
			result.rejectValue("name", e.getMessage(), e.getMessage());
			return newCity(city);
		}
		
		attributes.addFlashAttribute("message", "Cidade salva com sucesso!");
		return new ModelAndView("redirect:/cities/new");
	}
	
	@GetMapping
	public ModelAndView search(CityFilter filter, BindingResult result
			, @PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("city/search-cities");
		mv.addObject("states", stateRepository.findAll());
		
		PageWrapper<City> pageWrapper = new PageWrapper<>(citiesQuery.filtered(filter, pageable)
				, httpServletRequest);
		mv.addObject("page", pageWrapper);
		return mv;
	}
	
}
