package org.v8LogScannerWeb;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.v8LogScanner.appConfig.LogScannerConfig;
import org.v8LogScanner.appConfig.RootConfig;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.EventTypes;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.rgx.ScanProfile.DateRanges;
import org.v8LogScanner.rgx.ScanProfile.GroupTypes;
import org.v8LogScanner.rgx.ScanProfile.LogTypes;
import org.v8LogScanner.rgx.ScanProfile.RgxOpTypes;
import org.v8LogScanner.scanProfilesRepository.IScanProfileService;
import org.v8LogScanner.scanProfilesRepository.RegExpHib;
import org.v8LogScanner.scanProfilesRepository.ScanProfileHib;

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
  private IScanProfileService scanProfileService;
  
<<<<<<< HEAD
=======
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
  
>>>>>>> 84e0f952ef4134359f3a7cb6a5598a9e918a653f
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
    profile.getLogPaths().add("c://share");
    profile.getLogPaths().add("c://share2");
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
    
<<<<<<< HEAD
    // 2. check finding
    scanProfileService.resetCache();
    ScanProfile persistentProfile = scanProfileService.find(profile);
=======
    // 2/ check finding
    // we use native query because of caching
    Query<ScanProfileHib> queryProf= sessionFactory.getCurrentSession().createNativeQuery("SELECT TOP 1 * FROM ScanProfileHib ORDER BY ID DESC", 
      ScanProfileHib.class);
    ScanProfileHib persistentProfile = queryProf.getResultList().get(0);
>>>>>>> 84e0f952ef4134359f3a7cb6a5598a9e918a653f
   
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
    
<<<<<<< HEAD
    // 3. check deleting
    scanProfileService.remove(persistentProfile);
    Query<ScanProfileHib> query = sessionFactory.getCurrentSession().createQuery("from ScanProfileHib AS profiles WHERE profiles.id =:id", 
        ScanProfileHib.class);
    query.setParameter("id", profile.getId());
    assertEquals(0, query.getResultList().size());
=======
    //3. Check removing 
    scanProfileService.remove(persistentProfile);
    Query query = sessionFactory.getCurrentSession().createQuery("from ScanProfileHib AS profiles WHERE profiles.id =:id");
    query.setParameter("id", profile.getId());
    assertEquals(query.getResultList().size(), 0);
>>>>>>> 84e0f952ef4134359f3a7cb6a5598a9e918a653f
    
  }
  
}


