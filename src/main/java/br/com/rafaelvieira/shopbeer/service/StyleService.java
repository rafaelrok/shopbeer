package br.com.rafaelvieira.shopbeer.service;

import br.com.rafaelvieira.shopbeer.domain.Style;
import br.com.rafaelvieira.shopbeer.repository.StyleRepository;
import br.com.rafaelvieira.shopbeer.repository.filter.StyleFilter;
import br.com.rafaelvieira.shopbeer.repository.query.style.StylesQuery;
import br.com.rafaelvieira.shopbeer.service.exception.NameStyleAlreadyRegisteredException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StyleService {

    private final StyleRepository styleRepository;
    private final StylesQuery stylesQuery;

    public StyleService(StyleRepository styleRepository, StylesQuery stylesQuery) {
        this.styleRepository = styleRepository;
        this.stylesQuery = stylesQuery;
    }

    @Transactional(readOnly = true)
    public Style filtered(StyleFilter filter) {
        return stylesQuery.filtered(filter, null).getContent().get(0);

    }

    @Transactional
    public Style save(Style style) {
        Optional<Style> styleOptional = styleRepository.findByNameIgnoreCase(style.getName());
        if (styleOptional.isPresent()) {
            throw new NameStyleAlreadyRegisteredException("Name of the style already registered");
        }

        return styleRepository.saveAndFlush(style);
    }

}