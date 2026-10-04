package com.howtodoinjava.app.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller: the return value is written to the response body as JSON, no view is rendered.
 */
@RestController
public class HelloRestController {

  public record Greeting(String message) {
  }

  // GET /api/hello -> {"message":"Hello, World!"}
  @GetMapping("/api/hello")
  public Greeting hello(@RequestParam(defaultValue = "World") String name) {
    return new Greeting("Hello, " + name + "!");
  }
}
