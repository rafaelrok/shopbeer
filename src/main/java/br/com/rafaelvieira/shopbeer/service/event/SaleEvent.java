package br.com.rafaelvieira.shopbeer.service.event;

import br.com.rafaelvieira.shopbeer.domain.Sale;
import lombok.Getter;

@Getter
public record SaleEvent(Sale sale) {
}
