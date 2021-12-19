package ar.main;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ar.jpa.JpaTasks;
import ar.web.WebAPI;

public class Main {

  public static void main(String[] args) {
    
    String tokenSecret = System.getProperty("secret");
    if (tokenSecret == null) {
      throw new IllegalArgumentException("secret value must be passed as a jvm argument");
    }
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-derby");

    SetUpDb setUp = new SetUpDb(emf);
    setUp.setUp();
    
    WebAPI servicio = new WebAPI(1235, new JpaTasks(emf), tokenSecret);
    servicio.start();
  }
}
