package br.com.rafaelvieira.shopbeer.repository.query.city;

import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.repository.filter.CityFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CitiesQuery {

	Page<City> filtered(CityFilter filter, Pageable pageable);
	
}
