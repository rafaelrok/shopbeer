package br.com.rafaelvieira.shopbeer.repository.query.beer;

import java.util.List;

import br.com.rafaelvieira.shopbeer.domain.Beer;
import br.com.rafaelvieira.shopbeer.domain.dto.BeerDTO;
import br.com.rafaelvieira.shopbeer.domain.dto.StockItemValue;
import br.com.rafaelvieira.shopbeer.repository.filter.BeerFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeersQuery {

	Page<Beer> filtered(BeerFilter filter, Pageable pageable);
	
	List<BeerDTO> bySkuOrName(String skuOrName);
	
	StockItemValue stockItemValue();
	
}
