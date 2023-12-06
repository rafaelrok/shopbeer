package br.com.rafaelvieira.shopbeer.service;

import br.com.rafaelvieira.shopbeer.domain.Style;
import br.com.rafaelvieira.shopbeer.repository.StyleRepository;
import br.com.rafaelvieira.shopbeer.service.exception.NameStyleAlreadyRegisteredException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StyleService {

    private final StyleRepository styleRepository;

    public StyleService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
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