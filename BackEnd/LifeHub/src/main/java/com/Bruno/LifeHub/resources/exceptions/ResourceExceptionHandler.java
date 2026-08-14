package com.Bruno.LifeHub.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.Bruno.LifeHub.services.exceptions.EmailAlreadyExistsException;
import com.Bruno.LifeHub.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {

		int status = HttpStatus.NOT_FOUND.value();

		StandardError err = new StandardError();

		err.setTimestamp(Instant.now());
		err.setStatus(status);
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

		int status = HttpStatus.BAD_REQUEST.value();

		StandardError err = new StandardError();

		err.setTimestamp(Instant.now());
		err.setStatus(status);
		err.setError("Erro de validação");
		err.setMessage("Dados inválidos");
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<StandardError> validation(EmailAlreadyExistsException e, HttpServletRequest request) {

		int status = HttpStatus.BAD_REQUEST.value();

		StandardError err = new StandardError();

		err.setTimestamp(Instant.now());
		err.setStatus(status);
		err.setError("E-mail já cadastrado");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardError> generic(Exception e, HttpServletRequest request) {

		int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

		StandardError err = new StandardError();

		err.setTimestamp(Instant.now());
		err.setStatus(status);
		err.setError("Internal server error");
		err.setMessage("Ocorreu um erro inesperado");
		err.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}

}
