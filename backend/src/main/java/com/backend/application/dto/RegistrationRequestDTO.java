package com.backend.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegistrationRequestDTO {

  @NotNull
  @Size(min = 3)
  private String firstname;

  @NotNull
  @Size(min = 4)
  private String lastname;

  @NotNull
  @Size(min = 8)
  private String password;

  @NotNull
  @Email
  private String email;
}
