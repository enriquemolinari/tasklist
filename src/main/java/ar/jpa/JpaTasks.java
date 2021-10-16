package ar.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import ar.api.Tasks;
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

}
