package com.howtodoinjava.web;

import com.howtodoinjava.model.Item;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

  @PostMapping("/v1/items")
  public ResponseEntity<Item> create_v1(Item item) {

    //save item in database which sets the id field
    item.setId(11L);
    return ResponseEntity.ok(item);
  }

  @PostMapping("/v2/items")
  public ResponseEntity<Void> create_v2(Item item) {

    //save item in database which sets the id field
    item.setId(11L);
    return ResponseEntity.created(URI.create("/v2/items" + item.getId()).normalize()).build();
  }

  @PostMapping("/v3/items")
  public ResponseEntity<Item> create_v3(Item item) {

    //save item in database which sets the id field
    item.setId(11L);
    return ResponseEntity.status(201).body(item);
  }
}
