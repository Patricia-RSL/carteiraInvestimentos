package com.backend.application.services;

import com.backend.application.JwtUtil;
import com.backend.application.dto.RegistrationRequestDTO;
import com.backend.application.entities.User;
import com.backend.application.enums.UserRole;
import com.backend.application.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final UserService userService;
  private UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtUtil jwtUtil;
  private final AuthenticationManager authenticationManager;

  public User registerUser(RegistrationRequestDTO request) {
    if(Boolean.TRUE.equals(this.userRepository.existsByEmail((request.getEmail())))){
      throw new IllegalStateException("User already exists");
    }
    String hashedPassword = bCryptPasswordEncoder.encode(request.getPassword());

    User user = new User(
      request.getFirstName(),request.getLastName(),request.getEmail(),hashedPassword, UserRole.USER
    );

    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    user.setEnabled(true);
    user.setRole(UserRole.USER);

    return userRepository.save(user);
  }

  public String authenticate(String email, String password) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(email, password)
    );

    final UserDetails userDetails = userService.loadUserByUsername(email);
    return jwtUtil.generateToken(userDetails);
  }

}
