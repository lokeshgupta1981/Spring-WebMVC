package com.howtodoinjava.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MVC controller: puts a message into the model and returns the name of the Thymeleaf view.
 */
@Controller
public class HelloController {

  // GET /hello            -> Hello, World!
  // GET /hello?name=Lokesh  -> Hello, Lokesh!
  @GetMapping("/hello")
  public String hello(@RequestParam(defaultValue = "World") String name, Model model) {
    model.addAttribute("message", "Hello, " + name + "!");
    return "hello";   // view name -> templates/hello.html
  }

  // GET /hello/Lokesh -> Hello, Lokesh!
  @GetMapping("/hello/{name}")
  public String helloPath(@PathVariable String name, Model model) {
    model.addAttribute("message", "Hello, " + name + "!");
    return "hello";
  }
}
