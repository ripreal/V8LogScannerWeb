package org.v8LogScanner.dbLayer.scanProfilesPersistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.v8LogScanner.dbLayer.genericRepository.DataRepository;
import org.v8LogScanner.dbLayer.genericRepository.QuerySpecification;
import org.v8LogScanner.dbLayer.genericRepository.ScanProfileService;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.Specifications.ScanProfileHibSpecByID;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.Specifications.ScanProfileHibSpecByName;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.Specifications.ScanProfileHibSpecIfPresent;
import org.v8LogScanner.rgx.ScanProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ScanProfileHibService implements ScanProfileService {

    @Autowired
    private DataRepository<ScanProfileHib> repository;

    public ScanProfileHibService(DataRepository<ScanProfileHib> repository) {
        this.repository = repository;
    }

    @Override
    public int add(ScanProfile profile) {
        return (int) repository.add((ScanProfileHib) profile);
    }

    @Override
    public void remove(ScanProfile profile) {
        repository.remove((ScanProfileHib) profile);
    }

    @Override
    public void update(ScanProfile profile) {
        repository.update((ScanProfileHib) profile);
    }

    @Override
    public ScanProfile find(ScanProfile profile) {

        QuerySpecification<ScanProfileHib> spec = new ScanProfileHibSpecByName(profile.getName());
        List<ScanProfileHib> profiles = repository.query(spec);
        if (profiles.size() > 0) {
            ScanProfile prof = profiles.get(0);
            return prof;
        }
        else
            return null;
    }

    @Override
    public ScanProfile find(int id) {
        QuerySpecification<ScanProfileHib> spec = new ScanProfileHibSpecByID(id);
        List<ScanProfileHib> profiles = repository.query(spec);
        if (profiles.size() > 0)
            return (ScanProfile) profiles.get(0);
        else
            return null;
    }

    @Override
    public ScanProfile findIfPresent() {

        ScanProfile profile = null;

        QuerySpecification<ScanProfileHib> spec = new ScanProfileHibSpecIfPresent();
        List<ScanProfileHib> profiles = repository.query(spec);

        try {
            profile = Collections.max(profiles, Comparator.comparing(ScanProfileHib::getId));
        } catch (RuntimeException e){
            // no needs
        }
        return profile;
    }

    @Override
    public List<ScanProfile> getAll() {

        QuerySpecification<ScanProfileHib> spec = new ScanProfileHibSpecIfPresent();
        List<ScanProfileHib> profiles = repository.query(spec);

        List<ScanProfile> result = new ArrayList<>();
        profiles.forEach((el) -> result.add((ScanProfile) el));
        return result;
    }

    public void resetCache() {
        repository.resetCache();
    }
}
