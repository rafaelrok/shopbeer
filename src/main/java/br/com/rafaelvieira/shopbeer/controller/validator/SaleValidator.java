package br.com.rafaelvieira.shopbeer.controller.validator;

import java.math.BigDecimal;

import br.com.rafaelvieira.shopbeer.domain.Sale;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SaleValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Sale.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "costumer.code", "", "Select a customer in quick search");
		
		Sale sale = (Sale) target;
		validateIfInformedOnlyDeliveryTime(errors, sale);
		validateIfEnteredItems(errors, sale);
		validateNegativeTotalValue(errors, sale);
	}

	private void validateNegativeTotalValue(Errors errors, Sale sale) {
		if (sale.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			errors.reject("", "Total value cannot be negative");
		}
	}

	private void validateIfEnteredItems(Errors errors, Sale sale) {
		if (sale.getItens().isEmpty()) {
			errors.reject("", "Add at least one beer to the sale");
		}
	}

	private void validateIfInformedOnlyDeliveryTime(Errors errors, Sale sale) {
		if (sale.getDateTimeDelivery() != null && sale.getDeliveryDate() == null) {
			errors.rejectValue("deliveryDate", "", "Enter a delivery date for a time");
		}
	}

}
