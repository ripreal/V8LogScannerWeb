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
        try {
            DateRanges dateRangeType = DateRanges.valueOf(dateRange);
            String date = LogsOperations.getStartDate(dateRangeType, "");
            return ResponseEntity.status(HttpStatus.OK).body(date);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong format");
        }
    }

    @RequestMapping(value = "/endDateByDateRange", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<String> endDateByDateRange(@RequestParam(value = "dateRange") String dateRange) {
        try {
            DateRanges dateRangeType = DateRanges.valueOf(dateRange);
            String date = LogsOperations.getEndDate(dateRangeType, "");
            return ResponseEntity.status(HttpStatus.OK).body(date);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong format");
        }
    }

    @RequestMapping(value = "/propTypes", method = RequestMethod.GET,
            produces = "application/json")
    public List<PropTypes> propTypes(@RequestParam(value = "event") String event) {
        EventTypes eventType = null;
        eventType = EventTypes.valueOf(event);
        RegExp rgx = new RegExp(eventType);
        return rgx.getPropsForFiltering();
    }

}
