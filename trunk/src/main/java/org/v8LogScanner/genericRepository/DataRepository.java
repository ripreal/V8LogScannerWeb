package org.v8LogScanner.genericRepository;

import java.util.List;

public interface DataRepository<T> {
  
  public void add(T object);
  public void remove(T object);
  public void update(T object); // Think it as replace for set
  
  List<T> query(QuerySpecification<T> specification);
  
}
