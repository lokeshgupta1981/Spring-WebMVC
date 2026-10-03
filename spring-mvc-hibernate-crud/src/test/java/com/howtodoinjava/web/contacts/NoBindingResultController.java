package com.howtodoinjava.web.contacts;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/** Test-only controller: the same form handler without a BindingResult parameter. */
@Controller
class NoBindingResultController {

  @PostMapping("/test/no-binding-result")
  String create(@Valid @ModelAttribute("contact") Contact contact) {
    return "redirect:/contacts";
  }
}
