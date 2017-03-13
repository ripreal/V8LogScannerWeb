package org.v8LogScanner.genericRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Repository
@EnableTransactionManagement
public class HibernateRepository <T> implements DataRepository<T> {
  
  @Autowired
  private SessionFactory sessionFactory;
  private List<T> cached_data = new ArrayList<T>();
  private final int CACHE_LIMIT = 10;
  
  public HibernateRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void add(T data) {
    currentSession().save(data);
    updateCache(data);
  }
  
  public void remove(T data) {
    currentSession().remove(data);
    removeCache(data);
  }
  
  public void update(T data) {
    currentSession().update(data);
    updateCache(data);
  }

  public List<T> query(QuerySpecification<T> specification) {
    
    List<T> result = new ArrayList<>();
    
    for (T object : cached_data) {
      if (specification.specified(object))
        result.add(object);
    }
    
    if (result.size() == 0) {
      EntityManagerFactory entityManager = currentSession().getEntityManagerFactory();
      
      CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      CriteriaQuery<T> criteria = specification.toCriteria(builder);
      
      result = currentSession().createQuery(criteria).getResultList();
      
      saveCache(result.stream().limit(CACHE_LIMIT).collect(Collectors.toList()));
      
    }

    return result;
  }
  
  public void resetCache() {
    cached_data.clear();
  }
  
  private void saveCache(List<T> data) {
    for (T object : data) {
      if (data.indexOf(object) >= CACHE_LIMIT)
        break;
      cached_data.add(object);
    }
  }
  
  private void removeCache(T data) {
    cached_data.remove(data);
  }
  
  private void updateCache(T data) {
    removeCache(data);
    if (cached_data.size() < CACHE_LIMIT)
      cached_data.add(data);
  }
  
  private Session currentSession() {
    return sessionFactory.getCurrentSession();
  }  

}
