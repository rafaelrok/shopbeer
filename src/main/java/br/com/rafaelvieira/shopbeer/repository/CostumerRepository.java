package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CostumerRepository extends JpaRepository<Costumer, Long> {

    Optional<Costumer> findByCpfcnpj(String cpfcnpj);

    List<Costumer> findByNameStartingWithIgnoreCase(String name);

}
