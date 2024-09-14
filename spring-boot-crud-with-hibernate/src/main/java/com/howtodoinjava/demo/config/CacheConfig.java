package com.howtodoinjava.demo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public JCacheCacheManager cacheManager() {

    JCacheCacheManager cacheManager = new JCacheCacheManager();
    cacheManager.setCacheManager(Caching.getCachingProvider().getCacheManager());
    cacheManager.setTransactionAware(true); // Enable transaction awareness
    return cacheManager;
  }
}
