package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.Costumer;
import br.com.rafaelvieira.shopbeer.repository.query.costumer.CostumerQueries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CostumerRepository extends JpaRepository<Costumer, Long>, CostumerQueries {

    Optional<Costumer> findByCpfOrCnpj(String cpfOrCnpj);

    List<Costumer> findByNameStartingWithIgnoreCase(String name);

}
