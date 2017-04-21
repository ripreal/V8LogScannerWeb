package org.v8LogScannerWeb;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.io.IOException;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.v8LogScanner.appConfig.LogScannerConfig;
import org.v8LogScanner.appConfig.RootConfig;
import org.v8LogScanner.dbLayer.genericRepository.ScanProfileService;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.ScanProfileHib;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.EventTypes;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.rgx.ScanProfile.DateRanges;
import org.v8LogScanner.rgx.ScanProfile.GroupTypes;
import org.v8LogScanner.rgx.ScanProfile.LogTypes;
import org.v8LogScanner.rgx.ScanProfile.RgxOpTypes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
LogScannerConfig.class,
RootConfig.class})
@ActiveProfiles(profiles = "test")
public class DataSourceTest {
  
  @Autowired
  private DataSource datasource;
  @Autowired
  private SessionFactory sessionFactory;
  @Autowired
  private ScanProfileService scanProfileService;
  @Value(value = "classpath:schema.sql")
  private Resource schemasql;
  
  @Before
  public void setup() throws IOException {
    /* FOR PRODUCTION PROFILE
    InputStream in =  schemasql.getInputStream();
    InputStreamReader inputreader = new InputStreamReader(in);
    BufferedReader stringReader= new BufferedReader (inputreader);
    StringBuilder sb = new StringBuilder(); 
    CharBuffer buffer = CharBuffer.allocate(3096);
    stringReader.read(buffer);
    sb.append(buffer.array());
    
    JdbcTemplate template = new JdbcTemplate(datasource);
    
    String s = sb.toString();
    s = s.trim();
    
    template.execute(s);
    */
  }

  @Test
  public void dataSourceShouldNotBeNull() {
    assertNotNull(datasource);
  } 
  
  @Test
  public void ORMSessionShouldNotBeNull() {
    assertNotNull(sessionFactory);
  }
  
  @Test
  public void testJdbcQuery() {
    JdbcTemplate template = new JdbcTemplate(datasource);
    
    try {
      template.execute(
        "DROP TABLE IF EXISTS h2testtable;"
        +"CREATE TABLE h2testtable ("
        +"id VARCHAR(30),"
        +"name VARCHAR(20)"
        +");"
        +"");
      template.execute( "INSERT INTO h2testtable VALUES ('test completeted sucessful', 'k')");
      
      String testVal = template.queryForObject("SELECT * FROM h2testtable", (rs, rowNum) -> {
        return rs.getString("id");
      });
      
      assertNotNull(testVal, testVal);
      
    }
    catch (DataAccessException e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  @Transactional
  public void testScanProfilePersistence() {
    
    // 1. check adding 
    ScanProfile profile = new ScanProfileHib();
    profile.setName("test profile");
    profile.addLogPath("c://share");
    profile.addLogPath("c://share2");
    profile.setDateRange(DateRanges.LAST_HOUR);
    profile.setLimit(10);
    profile.setLogType(LogTypes.CLIENT);
    profile.setSortingProp(PropTypes.ApplicationName);
    profile.setGroupType(GroupTypes.BY_PROPS);
    profile.setRgxExp(".*test.*");
    profile.setRgxOp(RgxOpTypes.USER_OP);
    profile.setUserPeriod("16010123", "16020224");
    profile.addRegExp(new RegExp(EventTypes.CONN));
    profile.addRegExp(new RegExp(EventTypes.DBMSSQL));
    scanProfileService.add(profile);
    
    // 2. check finding
    scanProfileService.resetCache();
    ScanProfile persistentProfile = scanProfileService.find(profile);
   
    assertArrayEquals(profile.getLogPaths().toArray(new String[0]), persistentProfile.getLogPaths().toArray(new String[0]));
    assertEquals(DateRanges.LAST_HOUR, persistentProfile.getDateRange());
    assertEquals(10, persistentProfile.getLimit());
    assertEquals(LogTypes.CLIENT, persistentProfile.getLogType());
    assertEquals(PropTypes.ApplicationName, persistentProfile.getSortingProp());
    assertEquals(GroupTypes.BY_PROPS, persistentProfile.getGroupType());
    assertEquals(".*test.*", persistentProfile.getRgxExp());
    assertEquals(RgxOpTypes.USER_OP, persistentProfile.getRgxOp());
    assertArrayEquals(new String[]{"16010123", "16020224"}, persistentProfile.getUserPeriod());
    
    RegExp rgx = persistentProfile.getRgxList().get(0);
    assertEquals(EventTypes.CONN, rgx.getEventType());

    // 3. check deleting
    scanProfileService.remove(persistentProfile);
    Query<ScanProfileHib> query = sessionFactory.getCurrentSession().createQuery("from ScanProfileHib AS profiles WHERE profiles.id =:id", 
        ScanProfileHib.class);
    query.setParameter("id", profile.getId());
    assertEquals(0, query.getResultList().size());
  }
}


