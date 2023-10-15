package com.bashir.service;

import com.bashir.dto.PurchaseOrderRequestDto;
import com.bashir.dto.PurchaseOrderResponseDto;
import com.bashir.dto.RequestContext;
import com.bashir.integration.client.ProductClient;
import com.bashir.integration.client.UserClient;
import com.bashir.repository.PurchaseOrderRepository;
import com.bashir.util.EntityDtoUtil;
import com.bashir.util.SetTransactionRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@AllArgsConstructor
public class OrderFulfillmentService {

    private ProductClient productClient;
    private UserClient userClient;
    private SetTransactionRequestDto setTransactionRequestDto;
    private PurchaseOrderRepository repository;

    public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> purchaseOrderRequestDtoMono) {
      return purchaseOrderRequestDtoMono
             .map(requestDto -> RequestContext
                     .builder()
                     .purchaseOrderRequestDto(requestDto)
                     .build())
             .flatMap(this::getProduct)
             .doOnNext(requestContext -> setTransactionRequestDto.apply(requestContext))
             .flatMap(this::initiateTransaction)
             .map(rc -> EntityDtoUtil.getPurchaseOrder(rc))
             .map(purchaseOrder -> repository.save(purchaseOrder))
             .map(EntityDtoUtil::getPurchaseOrderResponseDto)
             .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<RequestContext> getProduct(final RequestContext requestContext) {
      return Mono.just(requestContext)
              .flatMap(context -> productClient.getProductById(context.getPurchaseOrderRequestDto().getProductId()))
              .map(productDto -> requestContext.builder()
                      .productDto(productDto)
                      .build());
    }

    public Mono<RequestContext> initiateTransaction(final RequestContext requestContext) {
        return Mono.just(requestContext)
                .flatMap(rc -> userClient.performTransaction(rc.getTransactionRequestDto()))
                .map(responseDto -> requestContext
                        .toBuilder()
                        .transactionResponseDto(responseDto)
                        .build());
    }


}
