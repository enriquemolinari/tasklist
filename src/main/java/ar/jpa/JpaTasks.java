package ar.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
      Task t = new Task(expirationDate, c, taskText);
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
