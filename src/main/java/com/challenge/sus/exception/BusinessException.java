package com.challenge.sus.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

  private String message;
  private HttpStatus status;

  /**
   * Request Exception.
   *
   * @param message errorCode.
   */
  public BusinessException(String message, HttpStatus status) {
    super(message);
    this.message = message;
    this.status = status;
  }
}
