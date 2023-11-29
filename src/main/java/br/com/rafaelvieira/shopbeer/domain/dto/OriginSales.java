package br.com.rafaelvieira.shopbeer.domain.dto;

import lombok.Data;

@Data
public class OriginSales {

	private String month;
	private Integer totalNational;
	private Integer totalInternational;
	
	public OriginSales() {
		
	}

	public OriginSales(String month, Integer totalNational, Integer totalInternational) {
		this.month = month;
		this.totalNational = totalNational;
		this.totalInternational = totalInternational;
	}
}
