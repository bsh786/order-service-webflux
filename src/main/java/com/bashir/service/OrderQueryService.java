package com.bashir.service;

import com.bashir.dto.PurchaseOrderResponseDto;
import com.bashir.entity.PurchaseOrder;
import com.bashir.repository.PurchaseOrderRepository;
import com.bashir.util.EntityDtoUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class OrderQueryService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public Flux<PurchaseOrderResponseDto> getAllPurchaseOrders(final Integer userId) {
          return Flux.fromStream(() -> purchaseOrderRepository.findByUserId(userId).stream())
                  .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                  .subscribeOn(Schedulers.boundedElastic());
    }
}
