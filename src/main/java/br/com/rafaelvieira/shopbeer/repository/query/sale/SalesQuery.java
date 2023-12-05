package br.com.rafaelvieira.shopbeer.repository.query.sale;

import java.math.BigDecimal;
import java.util.List;

import br.com.rafaelvieira.shopbeer.domain.Sale;
import br.com.rafaelvieira.shopbeer.domain.dto.OriginSalesDTO;
import br.com.rafaelvieira.shopbeer.domain.dto.SalesMonthDTO;
import br.com.rafaelvieira.shopbeer.repository.filter.SaleFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalesQuery {

	Page<Sale> filtered(SaleFilter filter, Pageable pageable);
	Sale searchWithItems(Long code);
	BigDecimal totalValueInYear();
	BigDecimal totalValueInMonth();
	BigDecimal averageTicketValueInYear();
	List<SalesMonthDTO> totalByMonth();
	List<OriginSalesDTO> totalByOrigin();
	
}
