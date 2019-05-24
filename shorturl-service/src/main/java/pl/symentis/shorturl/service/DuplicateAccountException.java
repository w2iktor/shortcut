package pl.symentis.shorturl.service;

public class DuplicateAccountException extends RuntimeException {

  public DuplicateAccountException(String message, Throwable cause) {
    super(message, cause);
  }

}
