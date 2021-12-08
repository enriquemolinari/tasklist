package ar.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TaskTest {

  @Test
  void futureStatusTask() {
    var creator = new Creator(1l, "aCreator");
    var task = new Task(1l, LocalDateTime.of(2021, 10, 02, 12, 15), "2022-04-12 14:30", creator,
        "do something very important !");

    var taskMap = task.toMap();
    assertEquals(1l, taskMap.get("id"));
    assertEquals(new DateTimeFormatted("2022-04-12 14:30").toString(), taskMap.get("expirationDate"));
    assertEquals("do something very important !", taskMap.get("text"));
    assertEquals(false, taskMap.get("done"));
    assertEquals(Map.of("creator", "aCreator"), taskMap.get("creator"));
    assertEquals("FUTURE", taskMap.get("status").toString());
  }
  
  @Test
  void fineStatusTask() {
    var creator = new Creator(1l, "aCreator");
    var task = new Task(1l, LocalDateTime.of(2021, 10, 02, 12, 15), "2021-10-09 14:30", creator,
        "do something very important !");

    var taskMap = task.toMap();
    assertEquals("FINE", taskMap.get("status").toString());
  }
  
  @Test
  void dangerStatusTask() {
    var creator = new Creator(1l, "aCreator");
    var task = new Task(1l, LocalDateTime.of(2021, 10, 02, 12, 15), "2021-10-03 14:30", creator,
        "do something very important !");

    var taskMap = task.toMap();
    assertEquals("DANGER", taskMap.get("status").toString());
  }

  @Test
  void warningStatusTask() {
    var creator = new Creator(1l, "aCreator");
    var task = new Task(1l, LocalDateTime.of(2021, 10, 02, 12, 15), "2021-10-05 14:30", creator,
        "do something very important !");

    var taskMap = task.toMap();
    assertEquals("WARNING", taskMap.get("status").toString());
  }

}
