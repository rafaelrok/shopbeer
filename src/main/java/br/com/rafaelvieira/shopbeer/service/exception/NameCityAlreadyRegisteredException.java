package br.com.rafaelvieira.shopbeer.service.exception;

public class NameCityAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NameCityAlreadyRegisteredException(String message) {
		super(message);
	}

}
