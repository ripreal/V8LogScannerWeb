package org.v8LogScanner.dbLayer.scanProfilesPersistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v8LogScanner.dbLayer.genericRepository.DataRepository;
import org.v8LogScanner.dbLayer.genericRepository.QuerySpecification;
import org.v8LogScanner.dbLayer.genericRepository.ScanProfileService;
import org.v8LogScanner.rgx.ScanProfile;

import java.util.List;

@Service
@Transactional
public class ScanProfileHibService implements ScanProfileService {

    @Autowired
    private DataRepository<ScanProfileHib> repository;

    public ScanProfileHibService(DataRepository<ScanProfileHib> repository) {
        this.repository = repository;
    }

    public void add(ScanProfile profile) {
        repository.add((ScanProfileHib) profile);
    }

    public void remove(ScanProfile profile) {
        repository.remove((ScanProfileHib) profile);
    }

    public void update(ScanProfile profile) {
        repository.update((ScanProfileHib) profile);
    }

    public ScanProfile find(ScanProfile profile) {
        return (ScanProfile) find(profile.getId());
    }

    public ScanProfile find(int id) {
        QuerySpecification<ScanProfileHib> spec = new ScanProfileHibSpecByID(id);
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
