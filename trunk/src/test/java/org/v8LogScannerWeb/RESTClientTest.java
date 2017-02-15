package org.v8LogScannerWeb;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.scanProfilesRepository.IScanProfileService;
import org.v8LogScanner.scanProfilesRepository.ScanProfileHib;
import org.v8LogScanner.webAppControllers.RESTClient;

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
  private IScanProfileService scanProfileService;
  @Autowired
  private ScanProfile profile;
  
  @Test
  public void beansInitialized() {
    assertNotNull(scanProfileService);
    assertNotNull(profile);
  }
  
  @Test
  public void testScanLogInCfg() throws Exception{
      
    RESTClient client = new RESTClient(profile);
    // MockMVC test
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
    mockMvc.
      perform(get("/scanLogPaths").accept(MediaType.APPLICATION_JSON)).
      andExpect(status().isOk());  
    
  }
  
  @Test
  public void testGetAllGroupTypes(){
      
    RESTClient client = new RESTClient(profile);
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
    
    ScanProfileHib profile = new ScanProfileHib();
    
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson=ow.writeValueAsString(profile);
    
    RESTClient client = mock(RESTClient.class);
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
    mockMvc.perform(post("/startRgxOp")
        .contentType(MediaType.APPLICATION_JSON).content(requestJson)
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    
  }
}
