package br.com.rafaelvieira.shopbeer.service.exception;

public class PasswordRequiredUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PasswordRequiredUserException(String message) {
		super(message);
	}

}
