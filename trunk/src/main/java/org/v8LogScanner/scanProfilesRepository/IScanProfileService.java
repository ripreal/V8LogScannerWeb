package org.v8LogScanner.scanProfilesRepository;

public interface IScanProfileService {
  
  public void add(ScanProfileHib profile);
  public void remove(ScanProfileHib profile);
  public void update(ScanProfileHib profile); // Think it as replace for set
  
  public ScanProfileHib find(ScanProfileHib profile);
  
}
