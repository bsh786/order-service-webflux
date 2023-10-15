package com.bashir.util;

import com.bashir.dto.OrderStatus;
import com.bashir.dto.PurchaseOrderResponseDto;
import com.bashir.dto.RequestContext;
import com.bashir.dto.TransactionStatus;
import com.bashir.entity.PurchaseOrder;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static PurchaseOrder getPurchaseOrder(final RequestContext requestContext) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
        purchaseOrder.setProductId(requestContext.getProductDto().getId());
        purchaseOrder.setAmount(requestContext.getTransactionResponseDto().getAmount());

        OrderStatus orderStatus = requestContext.getTransactionResponseDto()
                                     .getTransactionStatus()
                                     .equals(TransactionStatus.APPROVED) ?
                                      OrderStatus.COMPLETED : OrderStatus.FAILED;

       purchaseOrder.setStatus(orderStatus);
       return purchaseOrder;
    }

    public static PurchaseOrderResponseDto getPurchaseOrderResponseDto(PurchaseOrder purchaseOrder) {
        PurchaseOrderResponseDto purchaseOrderResponseDto = new PurchaseOrderResponseDto();
        BeanUtils.copyProperties(purchaseOrder, purchaseOrderResponseDto);
        purchaseOrderResponseDto.setOrderId(purchaseOrder.getId());
        return purchaseOrderResponseDto;
    }
}
