package org.v8LogScanner.appConfig;


import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan({"org.v8LogScanner.dbLayer.scanProfilesPersistence", "org.v8LogScanner.dbLayer.genericRepository"})
public class RootConfig {

    @Bean
    @Profile("test")
    public DataSource embeddedDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL).setName("yardnout")
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
    }

    @Bean
    @Profile("production")
    public DataSource productionDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:file:hsqlbase/db_data");
        dataSource.setUsername("SA");
        dataSource.setPassword("");

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {

        //LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        //
        //Properties properties = new Properties();
        //properties.put("hibernate.show_sql", "true");
        //properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        //
        //sessionBuilder.addProperties(properties);
        //SessionFactory factory = sessionBuilder.buildSessionFactory();
        LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
        sfb.setDataSource(dataSource);
        sfb.setPackagesToScan(new String[]{"org.v8LogScanner.dbLayer.scanProfilesPersistence"});
        Properties props = new Properties();
        props.setProperty("dialect", "org.hibernate.dialect.HSQLDialect");
        props.setProperty("hibernate.connection.url", "jdbc:hsqldb:file:hsqlbase/db_data");
        props.setProperty("show_sql", "true");
        sfb.setHibernateProperties(props);

        return sfb;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

        return transactionManager;
    }

}