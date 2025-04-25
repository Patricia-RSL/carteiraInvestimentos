package com.backend.application.services;

import com.backend.application.entities.UserTrade;
import com.backend.application.enums.OperationType;
import com.backend.application.repository.UserTradeRepository;
import com.backend.application.repository.UserTradeSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserTradeService {

  private final UserTradeRepository userTradeRepository;
  private final AuthService authService;

  public UserTradeService(UserTradeRepository userTradeRepository, AuthService authService) {
    this.userTradeRepository = userTradeRepository;
    this.authService = authService;
  }

  private Long getCurrentUserId() {
    return this.authService.getCurrentUser();
  }

  public List<UserTrade> getAll() {
    return userTradeRepository.findAllByUserId(getCurrentUserId());
  }

  public Optional<UserTrade> getById(Long id) {
    return userTradeRepository.findById(id);
  }

  public List<UserTrade> findAllByInstrument(String instrument) {
    return userTradeRepository.findAllByInstrumentAndUserId(instrument, getCurrentUserId());
  }

  public List<UserTrade> findAllByOperationType(OperationType tipo) {
    return userTradeRepository.findAllByOperationTypeAndUserId(tipo, getCurrentUserId());
  }

  public Page<UserTrade> getAllPaginatedAndFiltered(int page, int size, LocalDate beginDate, LocalDate endDate, List<String> instruments) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("date").descending());
    Specification<UserTrade> specification = Specification.where(UserTradeSpecifications.hasUserId(getCurrentUserId()));

    if (beginDate != null) {
      specification = specification.and(UserTradeSpecifications.hasDateGreaterThanOrEqualTo(beginDate));
    }

    if (endDate != null) {
      specification = specification.and(UserTradeSpecifications.hasDateLessThanOrEqualTo(endDate));
    }

    if (instruments != null && !instruments.isEmpty()) {
      specification = specification.and(UserTradeSpecifications.hasInstrumentIn(instruments));
    }

    return userTradeRepository.findAll(specification, pageRequest);
  }

  public void delete(Long id) {
    userTradeRepository.deleteById(id);
  }

  public UserTrade save(UserTrade userTrade) {
    return userTradeRepository.save(userTrade);
  }
}
