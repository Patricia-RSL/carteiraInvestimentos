package com.backend.application.services;

import com.backend.application.entities.User;
import com.backend.application.enums.UserRole;
import com.backend.application.repository.UserRepository;
import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private static final String USER_NOT_FOUND_MSG = "user with email %s not found";

  @Value("${admin.password}")
  private String adminPassword;

  @PostConstruct
  public void init() {
    Optional<User> adminOpt = userRepository.findByEmail("admin@admin.com");
    if (adminOpt.isEmpty()) {
      User admin = new User("Admin",
        "Admin", "admin@admin.com",
        passwordEncoder.encode(adminPassword),
        UserRole.ADMIN);
      userRepository.save(admin);
    } else {
      User admin = adminOpt.get();
      admin.setPassword(passwordEncoder.encode(adminPassword));
      userRepository.save(admin);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
      .orElseThrow(()->
        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
  }

}
