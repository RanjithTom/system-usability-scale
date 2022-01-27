package com.challenge.sus.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ErrorDetails implements Serializable {

  private static final long serialVersionUID = 1;
  private String message;
  private List<String> details;

  /**
   * Object to handle error code and message in response.
   *
   * @param message error message.
   */
  public ErrorDetails(String message) {
    this.message = message;
  }

  /**
   * Constructor to handle list of errors in the request, for javax validations.
   */
  public ErrorDetails(String message, List<String> details) {
    this.message = message;
    this.details = details;
  }
}
