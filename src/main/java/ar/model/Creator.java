package ar.model;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Creator {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String creatorName;
  
  public Creator(String creatorName) {
    this.creatorName = creatorName;
  }
  
  public String creatorName() {
    return this.creatorName;
  }
  
  public Map<String, String> toMap() {
    return Map.of("creator", this.creatorName);
  }
   
  //just for Hiberante
  
  public Creator() {
    
  }
  
  private Long getId() {
    return id;
  }

  private void setId(Long id) {
    this.id = id;
  }

  private String getCreatorName() {
    return creatorName;
  }

  private void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }  
}
