package org.v8LogScanner.scanProfilesRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v8LogScanner.genericRepository.DataRepository;
import org.v8LogScanner.genericRepository.QuerySpecification;
import org.v8LogScanner.rgx.ScanProfile;

@Service
@Transactional
public class ScanProfileService implements IScanProfileService {
  
  @Autowired
  private DataRepository<ScanProfileHib> repository;
  
  public ScanProfileService(DataRepository<ScanProfileHib> repository){
    this.repository = repository;
  }
  
<<<<<<< HEAD
  public void add(ScanProfile profile) {
    repository.add((ScanProfileHib) profile);
  }

  public void remove(ScanProfile profile) {
    repository.remove((ScanProfileHib)profile);
=======
  @Transactional
  public void add(ScanProfileHib profile) {
    repository.add(profile);
  }
  
  public void remove(ScanProfileHib profile) {
    repository.remove(profile);
>>>>>>> 84e0f952ef4134359f3a7cb6a5598a9e918a653f
  }

  public void update(ScanProfile profile) {
    repository.update((ScanProfileHib)profile);
  }
  
  public ScanProfile find(ScanProfile profile) {
    return (ScanProfile) find(profile.getId());
  }
  
  public ScanProfile find(int id) {
    QuerySpecification<ScanProfileHib> spec = new ScanProfileSpecByID(id);
    List<ScanProfileHib> profiles = repository.query(spec);
    if (profiles.size() > 0)
      return (ScanProfile) profiles.get(0);
    else
      return null;
  }
  
  public void resetCache() {
    repository.resetCache();
  }
}
