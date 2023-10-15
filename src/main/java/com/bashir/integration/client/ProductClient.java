package com.bashir.integration.client;

import com.bashir.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("productClient")
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(@Value("${client-service.product.service.url}") String url) {

        this.webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
    }

    public Mono<ProductDto> getProductById(final String productId) {
      return webClient
              .get()
              .uri("/{id}", productId)
              .retrieve()
              .bodyToMono(ProductDto.class);
    }
}
