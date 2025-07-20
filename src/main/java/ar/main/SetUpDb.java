package ar.main;

import java.time.LocalDateTime;
import java.util.UUID;


import ar.model.Creator;
import ar.model.DateTimeFormatted;
import ar.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

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

      Task u1 = new Task(LocalDateTime.now(), new DateTimeFormatted(LocalDateTime.now().plusDays(2)).toString(), c1,
          "Remember to do this, that, and also the other", UUID.randomUUID().toString());
      em.persist(u1);

      Task u2 = new Task(LocalDateTime.now(), new DateTimeFormatted(LocalDateTime.now().plusDays(1)).toString(), c1,
          "Buy that very important thing", UUID.randomUUID().toString());
      u2.done();
      em.persist(u2);

      Task u3 = new Task(LocalDateTime.now(), new DateTimeFormatted(LocalDateTime.now().plusDays(10)).toString(), c1,
          "Sister Birthday, do the preparations", UUID.randomUUID().toString());
      em.persist(u3);

      Task u4 = new Task(LocalDateTime.now(), new DateTimeFormatted(LocalDateTime.now().plusDays(20)).toString(), c1,
          "Record AC/DC interview", UUID.randomUUID().toString());
      em.persist(u4);

      Task u5 = new Task(LocalDateTime.now(), new DateTimeFormatted(LocalDateTime.now().plusDays(6)).toString(), c2,
          "Another non too important task", UUID.randomUUID().toString());
      em.persist(u5);

      Task u6 = new Task(LocalDateTime.now(), new DateTimeFormatted(LocalDateTime.now().plusDays(1)).toString(), c2,
          "This is very important", UUID.randomUUID().toString());
      em.persist(u6);

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw new RuntimeException(e);
    } finally {
      if (em.isOpen())
        em.close();
    }
  }
}
