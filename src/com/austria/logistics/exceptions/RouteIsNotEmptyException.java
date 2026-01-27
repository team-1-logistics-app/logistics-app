package com.austria.logistics.exceptions;

public class RouteIsNotEmptyException extends RuntimeException {
  public RouteIsNotEmptyException(String message) {
    super(message);
  }
}
