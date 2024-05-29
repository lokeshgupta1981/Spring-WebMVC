package com.howtodoinjava.retry.service;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.howtodoinjava.retry.service.exception.ServiceException;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
public class HelloWorldService {

	Logger logger = LoggerFactory.getLogger(HelloWorldService.class);

	@Autowired
	private WebClient webClient;

	public Mono<String> getResource() {
		return webClient.get().retrieve()
				.onStatus(HttpStatusCode::is5xxServerError,
						resp -> Mono.error(new ServiceException(resp.statusCode().toString())))
				.bodyToMono(String.class).retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
						.filter(ex -> ex instanceof ServiceException).onRetryExhaustedThrow((spec, signal) -> {
							throw new ServiceException(
									"Service call failed even after retrying " + signal.totalRetries() + " times");
						}).doBeforeRetry(x -> logger.info("Retrying " + x.totalRetries())));
	}
}
