package org.v8LogScannerWeb;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.v8LogScanner.appConfig.LogScannerConfig;
import org.v8LogScanner.appConfig.RootConfig;
import org.v8LogScanner.commonly.Filter;
import org.v8LogScanner.commonly.Filter.ComparisonTypes;
import org.v8LogScanner.dbLayer.genericRepository.ScanProfileService;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.FilterHib;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.LogsPathHib;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.RegExpHib;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.ScanProfileHib;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.EventTypes;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.webLayer.webAppControllers.RESTClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    LogScannerConfig.class,
    RootConfig.class
    })
@ActiveProfiles(profiles = "test")

public class RESTClientTest {
  
  @Autowired
  private ScanProfileService scanProfileService;
  @Autowired
  private ScanProfile profile;
  
  @Test
  public void beansInitialized() {
    assertNotNull(scanProfileService);
    assertNotNull(profile);
  }
  
  @Test
  public void testScanLogInCfg() throws Exception{
      
    RESTClient client = new RESTClient(scanProfileService);
    // MockMVC test
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
    mockMvc.
      perform(get("/scanLogPaths").accept(MediaType.APPLICATION_JSON)).
      andExpect(status().isOk());  
    
  }
  
  @Test
  public void testGetAllGroupTypes(){
      
    RESTClient client = new RESTClient(scanProfileService);
    // MockMVC test
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
    try {
      mockMvc.
        perform(get("/groupTypes")).
        andExpect(status().isOk());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Test
  public void testStartRgxOp() throws Exception {
    
    RESTClient client = new RESTClient(scanProfileService);
    
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
    mockMvc.perform(get("/startRgxOp")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
    
  }
  
  @Test
  public void testSetProfile() throws Exception {
    
    ScanProfile tPtofile = new ScanProfileHib();
    RegExpHib rgx = new RegExpHib(EventTypes.CONN);
    
    Map<PropTypes, Filter<String>> filters = new HashMap<>();
    FilterHib newFilter = new FilterHib();
    newFilter.add("2342");
    filters.put(PropTypes.Time, newFilter);
    
    //rgx.setFilters(filters);
    
    tPtofile.addLogPath("c:\test");
    tPtofile.addLogPath("c:\test2");
    tPtofile.addRegExp(rgx);
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(tPtofile);
    
    RESTClient client = new RESTClient(scanProfileService);
    
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
    mockMvc.perform(post("/setProfile")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson)      
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }
  
  @Test
  public void testSetLogs() throws Exception {
    
    List<LogsPathHib> logsPathTable = new ArrayList<>();
    
    LogsPathHib logsPath1 = new LogsPathHib();
    logsPath1.setPath("c:\\fakePath1");
    logsPath1.setServer("127.0.0.1");
    logsPathTable.add(logsPath1);
    
    LogsPathHib logsPath2 = new LogsPathHib();
    logsPath2.setPath("c:\\fakePath2");
    logsPath2.setServer("192.168.0.1");
    logsPathTable.add(logsPath2);
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(logsPathTable);
    
    RESTClient client = new RESTClient(scanProfileService);
    
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
    mockMvc.perform(post("/setLogs")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson)      
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }
}
