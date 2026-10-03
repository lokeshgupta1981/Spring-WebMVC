package com.howtodoinjava.web.contacts;

public class DuplicateEmailException extends RuntimeException {

  public DuplicateEmailException(String email) {
    super("Email " + email + " is already used");
  }
}
