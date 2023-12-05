package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.Sale;
import br.com.rafaelvieira.shopbeer.repository.query.sale.SalesQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long>, SalesQuery {
}
