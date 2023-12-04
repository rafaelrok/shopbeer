package br.com.rafaelvieira.shopbeer.service.exception;

public class ImpossibleDeleteEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ImpossibleDeleteEntityException(String msg) {
		super(msg);
	}

}
