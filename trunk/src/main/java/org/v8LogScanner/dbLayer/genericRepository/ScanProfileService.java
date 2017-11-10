package org.v8LogScanner.dbLayer.genericRepository;

import org.v8LogScanner.rgx.ScanProfile;

public interface ScanProfileService {

    public void add(ScanProfile profile);

    public void remove(ScanProfile profile);

    public void update(ScanProfile profile);

    public void resetCache();

    public ScanProfile find(ScanProfile profile);

    public ScanProfile find(int id);

}
