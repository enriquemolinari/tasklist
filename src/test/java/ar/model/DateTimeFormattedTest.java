package ar.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DateTimeFormattedTest {

  @Test
  void toLocalDate() {
    var date = new DateTimeFormatted("2001-12-10 10:30");
    assertEquals(LocalDateTime.of(2001, 12, 10, 10, 30), date.toLocalDateTime());
  }

  @Test
  void toStringTest() {
    var date = new DateTimeFormatted("2001-12-10 10:30");
    assertEquals("2001-12-10 10:30", date.toString());
  }

}
