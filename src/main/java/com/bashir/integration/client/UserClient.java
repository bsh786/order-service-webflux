package com.bashir.integration.client;

import com.bashir.dto.TransactionRequestDto;
import com.bashir.dto.TransactionResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("userClient")
public class UserClient {
    private final WebClient webClient;
    public UserClient(@Value("${client-service.user-transaction.service.url}") String url) {
        this.webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
    }
    public Mono<TransactionResponseDto> performTransaction(final TransactionRequestDto transactionRequestDto) {
        return this.webClient
                .post()
                .bodyValue(transactionRequestDto)
                .retrieve()
                .bodyToMono(TransactionResponseDto.class);
    }
}
