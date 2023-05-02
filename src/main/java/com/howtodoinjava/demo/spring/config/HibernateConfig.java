package com.howtodoinjava.demo.spring.config;

import java.util.Map;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScans(value = {@ComponentScan("com.howtodoinjava.demo.spring")})
public class HibernateConfig {

  @Bean
  public LocalSessionFactoryBean getSessionFactory() {
    LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
    factoryBean.setPackagesToScan("com.howtodoinjava.demo.spring.model");
    /*factoryBean.setConfigLocation(context.getResource("classpath:hibernate.cfg.xml"));*/
    Properties properties = new Properties();

    //Connection pool properties
    properties.putAll(Map.of(
        "hibernate.c3p0.min_size", "5",
        "hibernate.c3p0.max_size", "20",
        "hibernate.c3p0.acquire_increment", "2",
        "hibernate.c3p0.max_statements", "150",
        "hibernate.c3p0.timeout", "1800"
    ));

    //JPA properties
    properties.putAll(Map.of(
        "hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider",
        "hibernate.connection.username", "sa",
        "hibernate.connection.password", "password",
        "hibernate.connection.url", "jdbc:h2:mem:testdb",
        "hibernate.dialect", "org.hibernate.dialect.H2Dialect",
        "hibernate.connection.driver_class", "org.h2.Driver",
        "hibernate.hbm2ddl.auto", "create-drop",
        "hibernate.archive.autodetection", "class,hbm",
        "hibernate.show_sql", "true"
    ));

    factoryBean.setHibernateProperties(properties);
    return factoryBean;
  }

  @Bean
  public HibernateTransactionManager getTransactionManager() {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(getSessionFactory().getObject());
    return transactionManager;
  }
}
