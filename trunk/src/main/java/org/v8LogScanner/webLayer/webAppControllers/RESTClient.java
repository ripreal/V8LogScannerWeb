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
public class RESTClient {

    @Autowired
    private ScanProfileService scanProfileService;
    private final ProcessEvent procEvent = new ProcessEvent() {
        public void invoke(List<String> info) {
            log.addAll(info);
        }
    };
    private List<String> log = new ArrayList<>();

    public RESTClient(ScanProfileService scanProfileService) {
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No profiles found.");
    }

    @RequestMapping(value = "/groupTypes", method = RequestMethod.GET)
    public GroupTypes[] getAllGroupTypes() {
        return GroupTypes.values();
    }

    @RequestMapping(value = "/scanLogPaths", method = RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody
    List<String> scanLogsInCfgFile() {
        return LogsOperations.scanLogsInCfgFile();
    }

    @RequestMapping(value = "/eventTypes", method = RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody
    EventTypes[] eventTypes() {
        return EventTypes.values();
    }

    @RequestMapping(value = "/dateRanges", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<DateRanges[]> dateRanges() {
        return new ResponseEntity<>(DateRanges.values(), HttpStatus.OK);
    }

    @RequestMapping(value = "/startDateByDateRange", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> startDateByDateRange(@RequestParam(value = "dateRange") String dateRange) {
        DateRanges dateRangeType = null;
        try {
            dateRangeType = DateRanges.valueOf(dateRange);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        String date = LogsOperations.getStartDate(dateRangeType, "");
        return new ResponseEntity<>(date, HttpStatus.OK);
    }

    @RequestMapping(value = "/endDateByDateRange", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> endDateByDateRange(@RequestParam(value = "dateRange") String dateRange) {
        DateRanges dateRangeType = null;
        try {
            dateRangeType = DateRanges.valueOf(dateRange);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        String date = LogsOperations.getEndDate(dateRangeType, "");
        return new ResponseEntity<>(date, HttpStatus.OK);
    }

    @RequestMapping(value = "/propTypes", method = RequestMethod.GET,
            produces = "application/json")
    public List<PropTypes> propTypes(@RequestParam(value = "event") String event) {

        EventTypes eventType = null;
        eventType = EventTypes.valueOf(event);

        RegExp rgx = new RegExp(eventType);

        return rgx.getPropsForFiltering();
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
