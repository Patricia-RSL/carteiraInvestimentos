package com.backend.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegistrationRequestDTO {
  @NotNull
  private String firstname;
  @NotNull
  private String lastname;
  @NotNull
  @Size(min = 8)
  private String password;
  @NotNull
  @Email
  private String email;
}
