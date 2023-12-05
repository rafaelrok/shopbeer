package br.com.rafaelvieira.shopbeer.domain.dto;

import lombok.Data;

@Data
public class OriginSalesDTO {

	private String month;
	private Integer totalNational;
	private Integer totalInternational;
	
	public OriginSalesDTO() {
		
	}

	public OriginSalesDTO(String month, Integer totalNational, Integer totalInternational) {
		this.month = month;
		this.totalNational = totalNational;
		this.totalInternational = totalInternational;
	}
}
