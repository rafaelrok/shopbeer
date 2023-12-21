package br.com.rafaelvieira.shopbeer.controller.handler;

import br.com.rafaelvieira.shopbeer.service.exception.NameStyleAlreadyRegisteredException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {

	@ExceptionHandler(NameStyleAlreadyRegisteredException.class)
	public ResponseEntity<String> handleNameStyleAlreadyRegisteredException(NameStyleAlreadyRegisteredException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
}
