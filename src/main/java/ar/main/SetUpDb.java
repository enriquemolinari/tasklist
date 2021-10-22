package ar.main;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import ar.model.Creator;
import ar.model.DateTimeFormatted;
import ar.model.Task;

public class SetUpDb {

  private EntityManagerFactory emf;

  public SetUpDb(EntityManagerFactory emf) {
    this.emf = emf;
  }

  public void setUp() {

    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();

      Creator c1 = new Creator(1L, "juser");
      em.persist(c1);

      Creator c2 = new Creator(2L, "guser");
      em.persist(c2);

      Creator c3 = new Creator(3L, "admin");
      em.persist(c3);

      Task u1 = new Task(new DateTimeFormatted(LocalDateTime.now().plusDays(2)).toString(), c1,
          "Remember to do this, that, and also the other");
      em.persist(u1);

      Task u2 = new Task(new DateTimeFormatted(LocalDateTime.now().plusDays(1)).toString(), c1,
          "Buy that very important thing");
      em.persist(u2);

      Task u3 = new Task(new DateTimeFormatted(LocalDateTime.now().plusDays(10)).toString(), c1,
          "Sister Birthday, do the preparations");
      em.persist(u3);

      Task u4 = new Task(new DateTimeFormatted(LocalDateTime.now().plusDays(20)).toString(), c1,
          "Record AC/DC interview");
      em.persist(u4);

      Task u5 = new Task(new DateTimeFormatted(LocalDateTime.now().plusDays(6)).toString(), c2,
          "Another non too important task");
      em.persist(u5);

      Task u6 = new Task(new DateTimeFormatted(LocalDateTime.now().plusDays(1)).toString(), c2,
          "This is very important");
      em.persist(u6);

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw new RuntimeException(e);
    } finally {
      if (em != null && em.isOpen())
        em.close();
    }
  }
}
