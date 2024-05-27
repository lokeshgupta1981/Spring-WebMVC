package com.howtodoinjava.app.config;

import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

  @Bean("compressRestTemplate")
  public RestTemplate compressRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    restTemplate.getInterceptors().addFirst(new CompressingClientHttpRequestInterceptor());
    return restTemplate;
  }

  @Bean("compressWebClient")
  public WebClient compressWebClient() {
    ClientHttpConnector connector = new ReactorClientHttpConnector(HttpClient.create());

    return WebClient.builder()
        .clientConnector(connector)
        .filter(new CompressionFilter())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  @Bean
  public JettyServletWebServerFactory jettyServletWebServerFactory() {
    JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
    factory.addServerCustomizers(server -> {
      GzipHandler gzipHandler = new GzipHandler();
      gzipHandler.setInflateBufferSize(1);
      gzipHandler.setHandler(server.getHandler());

      HandlerCollection handlerCollection = new HandlerCollection(gzipHandler);
      server.setHandler(handlerCollection);
    });
    return factory;
  }
}
