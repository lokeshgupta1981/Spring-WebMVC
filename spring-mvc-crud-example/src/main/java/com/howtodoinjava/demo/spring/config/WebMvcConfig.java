package com.howtodoinjava.demo.spring.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.howtodoinjava.demo.spring"})
@PropertySource(value = "classpath:application.properties")
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    registry.jsp("/WEB-INF/views/jsp/", ".jsp").cache(true); // Configure JSP view resolver
    //registry.groovy().prefix("/WEB-INF/views/groovy/").suffix(".groovy").cache(true);
  }

 /* @Bean
  public GroovyMarkupConfigurer groovyMarkupConfigurer () {
    GroovyMarkupConfigurer configurer = new GroovyMarkupConfigurer();
    configurer.setResourceLoaderPath("/WEB-INF/views/groovy/");
    return configurer;
  }*/

  @Bean
  public InternalResourceViewResolver resolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setViewClass(JstlView.class);
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setSuffix(".jsp");
    return resolver;
  }

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource source = new ResourceBundleMessageSource();
    source.setBasename("messages");
    return source;
  }

  @Override
  public Validator getValidator() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setValidationMessageSource(messageSource());
    return validator;
  }
}
