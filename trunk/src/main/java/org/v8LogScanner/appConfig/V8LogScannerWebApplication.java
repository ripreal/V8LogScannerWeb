package org.v8LogScanner.appConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class V8LogScannerWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(V8LogScannerWebApplication.class, args);
  }
}
