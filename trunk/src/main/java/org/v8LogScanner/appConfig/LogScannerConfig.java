package org.v8LogScanner.appConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.v8LogScanner.dbLayer.scanProfilesPersistence.ScanProfileHib;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.rgx.ScanProfile.RgxOpTypes;

@Configuration
@ComponentScan({"org.v8LogScanner.WebClients", "org.v8LogScanner.dbLayer.scanProfilesPersistence"})
public class LogScannerConfig {
  
  @Bean
  public ScanProfile profile(){
    ScanProfile profile = new ScanProfileHib();
    profile.setRgxOp(RgxOpTypes.CURSOR_OP);
    return profile;
  }
}
