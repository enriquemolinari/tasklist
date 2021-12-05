package ar.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

class TaskExceptionTest {

  @Test
  void toErrorMap() {
    var e = new TaskException(Map.of("input1", "error1","input2", "error2"));
    assertEquals(Map.of("input1", "error1","input2", "error2"), e.toMap());
  }
  
}
