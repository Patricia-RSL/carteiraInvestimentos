package com.backend.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.backend.application.entities.UserTrade;
import com.backend.application.repository.UserTradeRepository;
import com.backend.application.enums.OperationType;

@Service
public class UserTradeService {

    private final UserTradeRepository userTradeRepository;

    public UserTradeService(UserTradeRepository userTradeRepository) {
        this.userTradeRepository = userTradeRepository;
    }

    public List<UserTrade> getAll() {
       return userTradeRepository.findAll();
    }

    public Optional<UserTrade> getById(Long id) {
        return userTradeRepository.findById(id);
    }

    public List<UserTrade> findAllByInstrument(String instrument){
        return userTradeRepository.findAllByInstrument(instrument);
    }

    public List<UserTrade> findAllByOperationType(OperationType tipo){
        return userTradeRepository.findAllByOperationType(tipo);
    }

    public void delete(Long id) {
        this.userTradeRepository.deleteById(id);
    }

    public UserTrade save(UserTrade userTrade) {
        return this.userTradeRepository.save(userTrade);
    }

    public Page<UserTrade> getAllPaginated(int page, int size) {
      Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
      return userTradeRepository.findAll(pageable);
    }
}
