package br.com.rafaelvieira.shopbeer.domain.dto;

import lombok.Data;

@Data
public class SalesMonth {

	private String month;
	private Integer total;

	public SalesMonth() {
	}

	public SalesMonth(String month, Integer total) {
		this.month = month;
		this.total = total;
	}

}
