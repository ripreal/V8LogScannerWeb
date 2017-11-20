package org.v8LogScanner.webLayer.webAppControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.v8LogScanner.commonly.ProcessEvent;
import org.v8LogScanner.dbLayer.genericRepository.ScanProfileService;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.LogsPathHib;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.ScanProfileHib;
import org.v8LogScanner.logs.LogsOperations;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.EventTypes;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.rgx.ScanProfile.DateRanges;
import org.v8LogScanner.rgx.ScanProfile.GroupTypes;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class ScanProfileController {

    @Autowired
    private ScanProfileService scanProfileService;

    public ScanProfileController(ScanProfileService scanProfileService) {
        this.scanProfileService = scanProfileService;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public ResponseEntity<Integer> setProfile(@RequestBody ScanProfileHib profile) {
        try {
            int id = scanProfileService.add(profile);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    public ResponseEntity<Integer> updateProfile(@RequestBody ScanProfileHib profile) {
        try {
            ScanProfile found = scanProfileService.find(profile);
            if (found != null)
                scanProfileService.update(profile);
            else
                scanProfileService.add(profile);
            return ResponseEntity.status(HttpStatus.OK).body(found.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "/profile/{id:\\d+}", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity getProfile(@PathVariable(value = "id", required = false) final int id) {
        ScanProfile profile = scanProfileService.find(id);
        if (profile != null)
            return ResponseEntity.status(HttpStatus.OK).body(profile);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    String.format("Scan profile with id %s no found.", id));
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity getProfileIfPresent() {
        ScanProfile profile = null;
        if ((profile = scanProfileService.findIfPresent()) != null)
            return ResponseEntity.status(HttpStatus.OK).body(profile);
        else
            return ResponseEntity.status(HttpStatus.OK).body(new ScanProfileHib());
    }

    @RequestMapping(value = "/profileNames", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity getProfileNames() {
        List<ScanProfile> profiles = scanProfileService.getAll();
        List<String> result = new ArrayList<>();
        profiles.forEach((element) -> result.add(element.getName()));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/startRgxOp", method = RequestMethod.GET)
    public ResponseEntity<Boolean> startRgxOp() {
    /*
    clientsManager.forEach(client -> {

      LanScanProfile lanProfile = (LanScanProfile) profile;
      ScanProfile cloned = lanProfile.clone();
      cloned.setLogPaths(client.getProfile().getLogPaths());
      client.setProfile(cloned);

    });
    clientsManager.forEach(client -> client.startRgxOp());

    */
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}


