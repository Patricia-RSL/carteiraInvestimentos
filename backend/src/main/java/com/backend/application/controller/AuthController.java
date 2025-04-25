package com.backend.application.controller;

import com.backend.application.dto.RegistrationRequestDTO;
import com.backend.application.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest ) {
    try {
      String token = authService.authenticate(loginRequest.email(), loginRequest.password());
      return ResponseEntity.ok(new AuthResponse(token));
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequestDTO request) {
    try {
      authService.registerUser(request);
      return ResponseEntity.ok().build();
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request ) {
    return ResponseEntity.ok("Logged out successfully");
  }
}
record AuthResponse(String token) {}
record LoginRequest(
  @NotBlank String email,
  @NotBlank String password
) {}

