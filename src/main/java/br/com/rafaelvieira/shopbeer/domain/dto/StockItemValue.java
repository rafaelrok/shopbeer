package br.com.rafaelvieira.shopbeer.domain.dto;

import lombok.Data;

@Data
public class StockItemValue {

	private Long value;
	private Long totalItens;
	
	public StockItemValue() {
	}

	public StockItemValue(Long value, Long totalItens) {
		this.value = value;
		this.totalItens = totalItens;
	}

	public Long getValue() {
		return value != null ? value : 0L;
	}

	public Long getTotalItens() {
		return totalItens != null ? totalItens : 0L;
	}
}
