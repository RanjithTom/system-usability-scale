package com.challenge.sus.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorDetails> handleBusinessException(BusinessException exception) {
    log.error("Exception occurred while processing ", exception);
    ErrorDetails errorDetails = new ErrorDetails(exception.getMessage());
    return createErrorResponse(errorDetails, exception.getStatus());
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class, ConversionFailedException.class,
      IllegalArgumentException.class})
  public ResponseEntity<ErrorDetails> handleMethodArgumentTypeMismatchException(
      Exception exception) {
    log.error("Exception occurred while processing ", exception);
    ErrorDetails errorDetails = new ErrorDetails(
        "Not able to process the Request, Make sure the request is as per contract");
    return createErrorResponse(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorDetails> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
    return createErrorResponse(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorDetails> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException ex) {
    ErrorDetails errorDetails = new ErrorDetails(ex.getMessage());
    return createErrorResponse(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    List<String> details = new ArrayList<>();
    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      details.add(error.getDefaultMessage());
    }
    ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), details);
    errorDetails.setMessage("Request validation errors");
    return createErrorResponse(errorDetails, HttpStatus.BAD_REQUEST);
  }

  private static ResponseEntity<ErrorDetails> createErrorResponse(
      ErrorDetails details, HttpStatus status) {
    return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(details);
  }

}
