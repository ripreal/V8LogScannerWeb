package org.v8LogScanner.scanProfilesRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v8LogScanner.genericRepository.DataRepository;
import org.v8LogScanner.genericRepository.QuerySpecification;

@Service
@Transactional
public class ScanProfileService implements IScanProfileService {
  
  @Autowired
  private DataRepository<ScanProfileHib> repository;
  
  public ScanProfileService(DataRepository<ScanProfileHib> repository){
    this.repository = repository;
  }
  
  @Transactional
  public void add(ScanProfileHib profile) {
    repository.add(profile);
  }
  
  public void remove(ScanProfileHib profile) {
    repository.remove(profile);
  }

  @Override
  public void update(ScanProfileHib profile) {
    repository.update(profile);
  }
  
  public ScanProfileHib find(ScanProfileHib profile) {
    return find(profile.getId());
  }
  
  public ScanProfileHib find(int id) {
    QuerySpecification<ScanProfileHib> spec = new ScanProfileSpecByID(id);
    List<ScanProfileHib> profiles = repository.query(spec);
    if (profiles.size() > 0)
      return profiles.get(0);
    else
      return null;
  }
}
