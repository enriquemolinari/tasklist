package ar.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

class CreatorTest {

  @Test
  void creatorToMap() {
    Creator c = new Creator(1l, "juser");
    assertEquals(Map.of("creator", "juser"), c.toMap());    
  }
  
}
