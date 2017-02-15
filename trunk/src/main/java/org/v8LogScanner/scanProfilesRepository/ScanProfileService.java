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
  
  @Override
  @Transactional
  public void add(ScanProfileHib profile) {
    repository.add(profile);
  }

  @Override
  public void remove(ScanProfileHib profile) {
    repository.remove(profile);
  }

  @Override
  public void update(ScanProfileHib profile) {
    repository.update(profile);
  }

  public ScanProfileHib find(ScanProfileHib profile) {
    QuerySpecification<ScanProfileHib> spec = new ScanProfileSpecByObject(profile);
    List<ScanProfileHib> profiles = repository.query(spec);
    if (profiles.size() > 0)
      return profiles.get(0);
    else
      return null;
  }
}
