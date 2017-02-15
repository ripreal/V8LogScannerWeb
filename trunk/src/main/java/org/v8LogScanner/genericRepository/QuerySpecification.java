package org.v8LogScanner.genericRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface QuerySpecification<T> {
  
  boolean specified(T profile);
  
  public CriteriaQuery<T> toCriteria(CriteriaBuilder builder);
  
}
