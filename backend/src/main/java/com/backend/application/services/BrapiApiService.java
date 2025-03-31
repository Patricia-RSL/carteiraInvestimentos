package com.backend.application.services;

import com.backend.application.dto.BrapiResponseDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Service
public class BrapiApiService {
  private final WebClient webClient;

  @Value("${brapi.api.key}")
  private String apiKey;

  public BrapiApiService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("https://brapi.dev/api/").build();
  }

  public BrapiResponseDTO getInstrumentQuote(String instrumentName) throws BadRequestException {
    if (this.apiKey == null || this.apiKey.isEmpty()) {
      throw new BadRequestException("An API key for Brapi is required to update data quotes. Please select an end date that already exists in the database.");
    }
    try {
      return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/quote/" + instrumentName)
          .queryParam("range", "3mo")
          .queryParam("interval", "1d")
          .queryParam("token", this.apiKey)
          .build())
        .retrieve()
        .bodyToMono(BrapiResponseDTO.class)
        .block();
    } catch (Exception e) {
      throw new BadRequestException("Error fetching data from Brapi API: " + e.getMessage());
    }
  }
}
