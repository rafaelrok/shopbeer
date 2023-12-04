package br.com.rafaelvieira.shopbeer.service.exception;

public class NameStyleAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NameStyleAlreadyRegisteredException(String message) {
		super(message);
	}

}
