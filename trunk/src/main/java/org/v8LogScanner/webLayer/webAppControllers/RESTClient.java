package org.v8LogScanner.webLayer.webAppControllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.v8LogScanner.rgx.AbstractOp;
import org.v8LogScanner.rgx.IRgxOp;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.EventTypes;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.rgx.ScanProfile.DateRanges;
import org.v8LogScanner.rgx.ScanProfile.GroupTypes;
import org.v8LogScanner.LocalTCPLogScanner.ClientsManager;
import org.v8LogScanner.LocalTCPLogScanner.LanScanProfile;
import org.v8LogScanner.LocalTCPLogScanner.V8LogScannerClient;
import org.v8LogScanner.LocalTCPLogScanner.ClientsManager.LogScannerClientNotFoundServer;
import org.v8LogScanner.commonly.ProcessEvent;
import org.v8LogScanner.dbLayer.genericRepository.ScanProfileService;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.LogsPathHib;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.ScanProfileHib;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.ScanProfileHibService;
import org.v8LogScanner.logs.LogsOperations;

@RestController
@CrossOrigin
public class RESTClient {
  
  @Autowired
  private ScanProfileService scanProfileService;
  private final ProcessEvent procEvent = new ProcessEvent(){
    public void invoke(List<String> info) {
      log.addAll(info);
    }
  };
  private List<String> log = new ArrayList<>();
  
  public RESTClient(ScanProfileService scanProfileService) {
    this.scanProfileService = scanProfileService;
  }
  
  @RequestMapping(value="/setProfile", method = RequestMethod.POST)
  public ResponseEntity<Integer> setProfile(@RequestBody ScanProfileHib profile) {
    //scanProfileService.add(profile);
    ResponseEntity<Integer> response = new ResponseEntity<>(1, HttpStatus.OK);
    return response;
  }
  
  @RequestMapping(value="/getProfile", method = RequestMethod.GET)
  @Transactional
  public ScanProfile getProfile(@RequestParam(value="id", defaultValue="0")int id) {
      ScanProfile profile = scanProfileService.find(id); 
      if (profile == null) {
        profile = new ScanProfileHib();
      }
      profile.addRegExp(new RegExp(EventTypes.CONN));
      profile.setLogPaths(scanLogsInCfgFile());
      return profile;      
  }
  
  @RequestMapping(value="/groupTypes", method = RequestMethod.GET)
  public GroupTypes[] getAllGroupTypes() {
      return GroupTypes.values();
  }

  @RequestMapping(value="/scanLogPaths", method = RequestMethod.GET,
    produces="application/json")
  public @ResponseBody List<String> scanLogsInCfgFile() {
    return LogsOperations.scanLogsInCfgFile();
  }
  
  @RequestMapping(value = "/setLogs", method = RequestMethod.POST)
  public ResponseEntity<String> setLogs(@RequestBody List<LogsPathHib> logsPath){
    //for(LogsPathHib path : logsPath) {
    //  try {
    //    V8LogScannerClient client = clientsManager.addClient(path.getServer(), procEvent);
    //    client.getProfile().addLogPath(path.getPath());
    //  }
    //  catch (ClientsManager.LogScannerClientNotFoundServer e) {
    //    return new ResponseEntity<>(e.getStackTrace().toString(), HttpStatus.BAD_REQUEST);
    //  }
   // }
    return new ResponseEntity<>("", HttpStatus.OK);
  }
  
  @RequestMapping(value = "/eventTypes", method = RequestMethod.GET,
    produces = "application/json")
  public @ResponseBody EventTypes[] eventTypes(){
    return EventTypes.values();
  }
  
  @RequestMapping(value = "/dateRanges", method = RequestMethod.GET,
      produces = "application/json")
  public ResponseEntity<DateRanges[]> dateRanges() {
    return new ResponseEntity<>(DateRanges.values(), HttpStatus.OK);
  }
  
  @RequestMapping(value = "/startDateByDateRange", method = RequestMethod.GET,
      produces = "application/json")
  public ResponseEntity<String> startDateByDateRange(@RequestParam(value= "dateRange") String dateRange) {
    DateRanges dateRangeType = null;
    try { 
      dateRangeType = DateRanges.valueOf(dateRange);
    }
    catch (Exception e){
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    String date = LogsOperations.getStartDate(dateRangeType, "");
    return new ResponseEntity<>(date, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/endDateByDateRange", method = RequestMethod.GET,
      produces = "application/json")
  public ResponseEntity<String> endDateByDateRange(@RequestParam(value= "dateRange") String dateRange) {
    DateRanges dateRangeType = null;
    try { 
      dateRangeType = DateRanges.valueOf(dateRange);
    }
    catch (Exception e){
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    String date = LogsOperations.getEndDate(dateRangeType, "");
    return new ResponseEntity<>(date, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/propTypes", method = RequestMethod.GET,
      produces = "application/json")
  public ResponseEntity<List<PropTypes>> propTypes(@RequestParam(value= "event")String event){
    
    EventTypes eventType = null;
    try { 
      eventType = EventTypes.valueOf(event);
    }
    catch (Exception e){
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    RegExp rgx = new RegExp(eventType);
    
    return new ResponseEntity<>(rgx.getPropsForFiltering(), HttpStatus.OK);
  }
  
  @RequestMapping(value = "/startRgxOp", method = RequestMethod.GET)
  public ResponseEntity<Boolean> startRgxOp(){
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
