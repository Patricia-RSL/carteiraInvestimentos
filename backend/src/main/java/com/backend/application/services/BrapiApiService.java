package com.backend.application.services;

import com.backend.application.dto.BrapiResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
public class BrapiApiService {
  private final WebClient webClient;

  public BrapiApiService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("https://brapi.dev/api/").build();
  }

  public BrapiResponseDTO getInstrumentQuote(String instrumentName) {

    return webClient.get()
      .uri(uriBuilder -> uriBuilder.path("/quote/"+instrumentName)
				.queryParam("range", "3mo")
				.queryParam("interval", "1d")
        .queryParam("token", "<you_api_key>")
        .build())
      .retrieve()
      .bodyToMono(BrapiResponseDTO.class)
      .block();
  }
}
