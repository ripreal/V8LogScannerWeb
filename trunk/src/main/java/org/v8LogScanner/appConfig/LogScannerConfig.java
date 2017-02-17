package org.v8LogScanner.appConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.scanProfilesRepository.ScanProfileHib;

@Configuration
@ComponentScan({"org.v8LogScanner.WebClients", "org.v8LogScanner.scanProfilesRepository"})
public class LogScannerConfig {
  
  @Bean
  public ScanProfile profile(){
    //ScanProfile profile = new ScanProfileHib();
    return null;
  }
}
