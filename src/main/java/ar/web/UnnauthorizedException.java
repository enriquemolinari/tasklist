package ar.web;

public class UnnauthorizedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public UnnauthorizedException(String msg) {
    super(msg);
  }
  
  public UnnauthorizedException(Exception e) {
    super(e);
  }
}
