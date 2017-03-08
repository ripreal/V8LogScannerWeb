package org.v8LogScanner.genericRepository;

import java.util.List;

import org.v8LogScanner.scanProfilesRepository.ScanProfileHib;

public interface DataRepository<T> {
  
  public void add(T object);
  public void remove(T object);
  public void update(T object); 
  public void resetCache();
  
  public List<T> query(QuerySpecification<T> specification);
  
}
