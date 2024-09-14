package com.howtodoinjava.demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonVO {

  private Long id;

  @NotBlank(message = "First name is required")
  @Size(min = 3, max = 20, message = "First name should be between 3 and 20 characters")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Size(min = 3, max = 20, message = "Last name should be between 3 and 20 characters")
  private String lastName;

  @NotBlank(message = "Email is required")
  @Email(message = "Email is invalid")
  private String email;
}
