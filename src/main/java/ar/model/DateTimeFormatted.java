package ar.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatted {

  private static String format = "yyyy-MM-dd HH:mm";
  private LocalDateTime dateTime;
  
  public DateTimeFormatted(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }
  
  public DateTimeFormatted(String dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    this.dateTime = LocalDateTime.parse(dateTime, formatter);
  }

  public String toString() {
    return this.dateTime.format(DateTimeFormatter.ofPattern(format));
  }
  
  public LocalDateTime toLocalDateTime() {
    return this.dateTime;
  }
}
