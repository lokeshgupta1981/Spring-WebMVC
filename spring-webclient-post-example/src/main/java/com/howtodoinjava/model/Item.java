package com.howtodoinjava.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item {

  Long id;
  String name;

  public Item(String name) {
    this.name = name;
  }
}
