package ar.model;

import java.util.Map;

public class TaskException extends RuntimeException {
  
  private final Map<String, String> errors;
  
  public TaskException(Map<String, String> errors) {
    this.errors = errors;
  }
   
  public Map<String, String> toMap() {
    return Map.copyOf(errors);
  }
}
