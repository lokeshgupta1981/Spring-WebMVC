package com.howtodoinjava.retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.howtodoinjava.retry.service.HelloWorldService;

import reactor.core.publisher.Mono;

@RestController
public class WebClientController {

	@Autowired
	private HelloWorldService helloWorldService;

	@GetMapping(value = "/helloWorldResource")
	public Mono<String> getResource() {
		return helloWorldService.getResource();
	}
	
}
