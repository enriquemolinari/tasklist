package ar.jpa;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ar.api.BulkData;
import ar.api.Tasks;
import ar.model.Creator;
import ar.model.Task;

public class JpaTasks implements Tasks {

  private EntityManagerFactory emf;

  public JpaTasks(EntityManagerFactory emf) {
    this.emf = emf;
  }

  @Override
  public List<Task> list(String idCreator) {
    EntityManager em = emf.createEntityManager();
    try {
      TypedQuery<Task> q = em.createQuery("select u from Task u where u.creator.id = :idcreator",
          Task.class);

      q.setParameter("idcreator", Long.valueOf(idCreator));
      return q.getResultList();

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (em != null && em.isOpen())
        em.close();
    }
  }

  @Override
  public void bulkUpdate(String idCreator, List<BulkData> data) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      // order of queued to apply changes
      Collections.sort(data);

      for (BulkData bulkData : data) {
        if ("add".equals(bulkData.operation())) {
          Creator c = em.getReference(Creator.class, Long.valueOf(idCreator));
          Task t = new Task(LocalDateTime.now(), bulkData.expirationDate(), c, bulkData.text(),
              bulkData.syncId());
          em.persist(t);
        } else if ("update".equals(bulkData.operation())) {
          Query query = em.createQuery(
              "update from Task t set t.done = :done where t.syncId = :syncId and t.creator.id = :idCreator");
          query.setParameter("syncId", bulkData.syncId());
          query.setParameter("idCreator", Long.valueOf(idCreator));
          query.setParameter("done", bulkData.done());
          query.executeUpdate();
          
        } else if ("del".equals(bulkData.operation())) {
          Query query = em
              .createQuery("delete from Task t where t.syncId = :syncId and t.creator.id = :idCreator");
          query.setParameter("syncId", bulkData.syncId());
          query.setParameter("idCreator", Long.valueOf(idCreator));
          query.executeUpdate();          
        }
      }
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw new RuntimeException(e);
    } finally {
      if (em.isOpen())
        em.close();
    }
  }

  @Override
  public void deleteTask(String idCreator, String idTask) {

    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      Query query = em.createQuery("delete from Task t where t.id = :id and t.creator.id = :idCreator");
      query.setParameter("id", Long.valueOf(idTask));
      query.setParameter("idCreator", Long.valueOf(idCreator));
      query.executeUpdate();
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw new RuntimeException(e);
    } finally {
      if (em.isOpen())
        em.close();
    }
  }

  @Override
  public void addTask(String idCreator, String taskText, String expirationDate) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      Creator c = em.getReference(Creator.class, Long.valueOf(idCreator));
      Task t = new Task(LocalDateTime.now(), expirationDate, c, taskText);
      em.persist(t);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw e;
    } finally {
      if (em.isOpen())
        em.close();
    }
  }

  @Override
  public void done(String idCreator, String idTask) {
    doneOrUnDone(idCreator, idTask, true);
  }

  @Override
  public void inProgress(String idCreator, String idTask) {
    doneOrUnDone(idCreator, idTask, false);
  }

  private void doneOrUnDone(String idCreator, String idTask, boolean done) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      Query query = em.createQuery(
          "update from Task t set t.done = :done where t.id = :id and t.creator.id = :idCreator");
      query.setParameter("id", Long.valueOf(idTask));
      query.setParameter("idCreator", Long.valueOf(idCreator));
      query.setParameter("done", done);
      query.executeUpdate();
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
