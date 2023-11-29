package br.com.rafaelvieira.shopbeer.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportingPeriod {

	private LocalDate startDate;
	private LocalDate endDate;
}
