package org.v8LogScanner.dbLayer.genericRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface QuerySpecification<T> {
  
  boolean specified(T object);
  
  public CriteriaQuery<T> toCriteria(CriteriaBuilder builder);
  
}
