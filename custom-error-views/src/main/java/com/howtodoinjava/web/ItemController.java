package com.howtodoinjava.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

@RestController
public class ItemController {

  @GetMapping("/items/{id}")
  public Item getItem(@PathVariable("id") Long id) {
    return new Item(id, "Test");
  }

  @GetMapping("/records/500")
  public void error() {
    var cause = new NullPointerException("Dummy Exception");
    throw new ServerErrorException(cause.getMessage(), cause);
  }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Item {
  Long id;
  String name;
}
