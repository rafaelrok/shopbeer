package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
}
