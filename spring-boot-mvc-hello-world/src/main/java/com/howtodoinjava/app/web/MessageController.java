package com.howtodoinjava.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {

  @GetMapping("/message")
  public String displayMessage(Model model) {

    model.addAttribute("message", "Hello, World!");
    return "messageView";
  }

  @GetMapping("/message-without-gzip-compression")
  public String displayMessageV2(Model model) {

    model.addAttribute("message", "Hello, World!");
    return "messageView";
  }
}
