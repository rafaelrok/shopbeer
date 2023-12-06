package br.com.rafaelvieira.shopbeer.service;
import java.util.Optional;

import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.repository.CityRepository;
import br.com.rafaelvieira.shopbeer.service.exception.NameCityAlreadyRegisteredException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional
    public void save(City city) {
        Optional<City> cidadeExistente = cityRepository.findByNameAndState(city.getName(), city.getState());
        if (cidadeExistente.isPresent()) {
            throw new NameCityAlreadyRegisteredException("City name already registered");
        }

        cityRepository.save(city);
    }
}
