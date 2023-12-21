package br.com.rafaelvieira.shopbeer.controller;

import br.com.rafaelvieira.shopbeer.controller.page.PageWrapper;
import br.com.rafaelvieira.shopbeer.domain.Style;
import br.com.rafaelvieira.shopbeer.repository.filter.StyleFilter;
import br.com.rafaelvieira.shopbeer.repository.query.style.StylesQuery;
import br.com.rafaelvieira.shopbeer.service.StyleService;
import br.com.rafaelvieira.shopbeer.service.exception.NameStyleAlreadyRegisteredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@RestController
@RequestMapping("/styles")
public class StyleController {

	private final StyleService styleService;
	private final StylesQuery stylesQuery;

    public StyleController(StyleService styleService, StylesQuery stylesQuery) {
        this.styleService = styleService;
        this.stylesQuery = stylesQuery;
    }

    @RequestMapping("/new")
	public ModelAndView newStyle(Style style) {
		return new ModelAndView("style/register-style");
	}
	
	@PostMapping(value = "/new")
	public ModelAndView register(@Valid Style style, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return newStyle(style);
		}
		
		try {
			styleService.save(style);
		} catch (NameStyleAlreadyRegisteredException e) {
			result.rejectValue("name", e.getMessage(), e.getMessage());
			return newStyle(style);
		}
		
		attributes.addFlashAttribute("message", "Style saved successfully!");
		return new ModelAndView("redirect:/styles/new");
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> save(@RequestBody @Valid Style style, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError("name")).getDefaultMessage());
		}
		
		style = styleService.save(style);
		return ResponseEntity.ok(style);
	}
	
	@GetMapping
	public ModelAndView search(StyleFilter filter, BindingResult result
			, @PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("style/search-styles");
		
		PageWrapper<Style> pageWrapper = new PageWrapper<>(stylesQuery.filtered(filter, pageable)
				, httpServletRequest);
		mv.addObject("page", pageWrapper);
		return mv;
	}
	
}
