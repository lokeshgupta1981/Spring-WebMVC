package com.howtodoinjava.web.contacts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Contact {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Name is required")
  @Size(max = 50, message = "Name must be at most 50 characters")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Email is not valid")
  @Column(unique = true)
  private String email;

  @Pattern(regexp = "[0-9 +-]{0,20}", message = "Phone may contain digits, spaces, + and - only")
  private String phone;

  @Size(max = 40, message = "City must be at most 40 characters")
  private String city;

  public Contact() {
  }

  public Contact(String name, String email, String phone, String city) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.city = city;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getCity() { return city; }
  public void setCity(String city) { this.city = city; }

  @Override
  public String toString() {
    return "Contact[id=" + id + ", name=" + name + ", email=" + email + "]";
  }
}
