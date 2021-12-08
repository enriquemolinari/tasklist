package ar.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TaskErrorsTest {

  @Test
  void textNullOrEmpty() {
    var taskError = new TaskErrors("", LocalDateTime.now().toString());
    try {
      taskError.throwOnError();
      fail();
    } catch (TaskException te) {
      assertEquals("This field is mandatory", te.toMap().get("taskText"));
    }
  }

  @Test
  void expirationDateNullOrEmpty() {
    var taskError = new TaskErrors("this is the text", "");
    try {
      taskError.throwOnError();
      fail();
    } catch (TaskException te) {
      assertEquals("This field is mandatory", te.toMap().get("expirationDate"));
    }
  }

  @Test
  void expirationDateInFuture() {
    var taskError = new TaskErrors("this is the text", "2021-12-05 18:00");
    try {
      taskError.throwOnError();
      fail();
    } catch (TaskException te) {
      assertEquals("Date must be in the future", te.toMap().get("expirationDate"));
    }
  }

  @Test
  void expirationDateParseError() {
    var taskError = new TaskErrors("this is the text", "2021-22-12 18:00");
    try {
      taskError.throwOnError();
      fail();
    } catch (TaskException te) {
      assertEquals("Date is invalid", te.toMap().get("expirationDate"));
    }
  }

}
