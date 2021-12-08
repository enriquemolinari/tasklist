package ar.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

class TaskErrors {

  private static final String EXPIRATION_DATE = "expirationDate";
  private static final String MSG_MISSING = "This field is mandatory";
  private static final String MSG_INVALID_DATE = "Date is invalid";
  private static final String MSG_PAST_DATE = "Date must be in the future";

  private String taskText;
  private String expirationDate;
  private LocalDateTime creationDate;
  private Map<String, String> errors = new HashMap<>();

  private boolean nullOrEmpty(String value) {
    return value == null || value.isEmpty() || value.isBlank();
  }

  public TaskErrors(LocalDateTime creationDate, String taskText, String expirationDate) {
    this.creationDate = creationDate;
    this.taskText = taskText;
    this.expirationDate = expirationDate;
  }

  public TaskErrors(String taskText, String expirationDate) {
    this(LocalDateTime.now(), taskText, expirationDate);
  }

  public void throwOnError() {

    if (nullOrEmpty(taskText)) {
      this.errors.put("taskText", MSG_MISSING);
    }

    if (nullOrEmpty(expirationDate)) {
      this.errors.put(EXPIRATION_DATE, MSG_MISSING);
    } else {
      try {
        var expDate = new DateTimeFormatted(expirationDate);
        if (this.creationDate.isAfter(expDate.toLocalDateTime())) {
          this.errors.put(EXPIRATION_DATE, MSG_PAST_DATE);
        }
      } catch (DateTimeParseException e) {
        this.errors.put(EXPIRATION_DATE, MSG_INVALID_DATE);
      }
    }

    if (this.hasErrors()) {
      throw new TaskException(this.toMap());
    }
  }

  private boolean hasErrors() {
    return !this.errors.isEmpty();
  }

  private Map<String, String> toMap() {
    return Map.copyOf(this.errors);
  }
}
