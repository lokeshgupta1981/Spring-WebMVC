package com.howtodoinjava.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class WebClientRetryApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebClientRetryApplication.class, args);
	}


}
