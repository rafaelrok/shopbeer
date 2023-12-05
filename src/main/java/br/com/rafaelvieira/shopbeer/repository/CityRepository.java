package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.City;
import br.com.rafaelvieira.shopbeer.domain.State;
import br.com.rafaelvieira.shopbeer.repository.query.city.CitiesQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long>, CitiesQuery {
    List<City> findByStatecode(Long codeState);

    Optional<City> findByNameAndState(String name, State state);
}
