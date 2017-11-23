package org.v8LogScannerWeb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import org.v8LogScanner.dbLayer.genericRepository.ScanProfileService;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.FilterHib;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.LogsPathHib;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.RegExpHib;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.ScanProfileHib;
import org.v8LogScanner.rgx.RegExp.EventTypes;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.webLayer.webAppControllers.RESTClient;
import org.v8LogScanner.webLayer.webAppControllers.ScanProfileController;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void testScanLogInCfg() throws Exception {

        RESTClient client = new RESTClient();
        // MockMVC test
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
        mockMvc.
                perform(get("/scanLogPaths").accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());

    }

    @Test
    public void testGetAllGroupTypes() throws Exception{

        RESTClient client = new RESTClient();
        // MockMVC test
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
        mockMvc.
                perform(get("/groupTypes")).
                andExpect(status().isOk());

    }

    @Test
    public void testStartRgxOp() throws Exception {

        ScanProfileController client = new ScanProfileController(scanProfileService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
        mockMvc.perform(get("/startRgxOp")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testSetProfile() throws Exception {

        ScanProfile tPtofile = new ScanProfileHib();
        RegExpHib rgx = new RegExpHib(EventTypes.CONN);

        FilterHib newFilter = new FilterHib();
        newFilter.add("2342");

        Map<PropTypes, Filter<String>> filters = rgx.getFilters();
        filters.put(PropTypes.Time, newFilter);
        rgx.setFilters(filters);

        tPtofile.addLogPath("c:\test");
        tPtofile.addLogPath("c:\test2");
        tPtofile.addRegExp(rgx);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(tPtofile);

        ScanProfileController client = new ScanProfileController(scanProfileService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
        mockMvc.perform(post("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProfile() throws Exception {

        ScanProfileService service = Mockito.mock(ScanProfileService.class);
        Mockito.when(service.find(0)).thenReturn(new ScanProfileHib());

        ScanProfileController client = new ScanProfileController(service);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();

        mockMvc.
                perform(get("/profile/0")).
                andExpect(status().isOk());

        mockMvc.
                perform(get("/profile/1")).
                andExpect(status().is4xxClientError());

        Mockito.when(service.findIfPresent()).thenReturn(new ScanProfileHib());

        mockMvc.
                perform(get("/profile")).
                andExpect(status().isOk());

        mockMvc.
                perform(get("/profile/")).
                andExpect(status().isOk());
    }

    @Test
    public void testGetProfileIfPresent() throws Exception {

        ScanProfileService service = Mockito.mock(ScanProfileService.class);

        ScanProfileController client = new ScanProfileController(scanProfileService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();

        mockMvc.
                perform(get("/profile")).
                andExpect(status().isOk());
    }

    @Test
    public void testGetProfileNames() throws Exception {

        ScanProfileService service = Mockito.mock(ScanProfileService.class);

        ScanProfileController client = new ScanProfileController(scanProfileService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();

        mockMvc.
                perform(get("/profileNames")).
                andExpect(status().isOk());
    }

    @Test
    public void testUpdateProfile() throws Exception {

        ScanProfile tPtofile = new ScanProfileHib();
        tPtofile.setName("seeking");
        RegExpHib rgx = new RegExpHib(EventTypes.CONN);

        FilterHib newFilter = new FilterHib();
        newFilter.add("2342");

        Map<PropTypes, Filter<String>> filters = rgx.getFilters();
        filters.put(PropTypes.Time, newFilter);
        rgx.setFilters(filters);

        tPtofile.addLogPath("c:\test");
        tPtofile.addLogPath("c:\test2");
        tPtofile.addRegExp(rgx);

        scanProfileService.add(tPtofile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(tPtofile);

        ScanProfileController client = new ScanProfileController(scanProfileService);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(client).build();
        mockMvc.perform(put("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

}


