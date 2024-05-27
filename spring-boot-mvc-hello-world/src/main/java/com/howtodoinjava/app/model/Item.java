package com.howtodoinjava.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;


public class Item {

  Long id;
  String name;
  public Item(){
  }
  public Item(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Item{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
