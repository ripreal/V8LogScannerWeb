package org.v8LogScanner.dbLayer.genericRepository;

import org.v8LogScanner.rgx.ScanProfile;

import java.util.List;

public interface ScanProfileService {

    public int add(ScanProfile profile);

    public void remove(ScanProfile profile);

    public void update(ScanProfile profile);

    public void resetCache();

    public ScanProfile find(ScanProfile profile);

    public ScanProfile find(int id);

    public ScanProfile findIfPresent();

    public List<ScanProfile> getAll();

}
