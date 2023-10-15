package com.bashir.http.handler;

import com.bashir.dto.PurchaseOrderRequestDto;
import com.bashir.dto.PurchaseOrderResponseDto;
import com.bashir.repository.PurchaseOrderRepository;
import com.bashir.service.OrderFulfillmentService;
import com.bashir.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderHandler {

    @Autowired
    OrderFulfillmentService fulfillmentService;
    @Autowired
    OrderQueryService queryService;

    public Mono<ServerResponse> createOrder(ServerRequest serverRequest) {
       return serverRequest.bodyToMono(PurchaseOrderRequestDto.class)
               .flatMap(requestDto -> fulfillmentService.processOrder(Mono.just(requestDto)))
               .flatMap(purchaseOrderResponseDto -> ServerResponse.ok().bodyValue(purchaseOrderResponseDto));
    }

    public Mono<ServerResponse> getAllOrders(ServerRequest serverRequest) {

        Flux<PurchaseOrderResponseDto> responseFlux = Mono.just(serverRequest.pathVariable("userId"))
                .map(id -> Integer.valueOf(id))
                .flatMapMany(id -> queryService.getAllPurchaseOrders(id));

        return ServerResponse.ok().body(responseFlux, PurchaseOrderRepository.class);
    }
}
