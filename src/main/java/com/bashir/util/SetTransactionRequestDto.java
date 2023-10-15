package com.bashir.util;

import com.bashir.dto.RequestContext;
import com.bashir.dto.TransactionRequestDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SetTransactionRequestDto implements Function<RequestContext, RequestContext> {

    @Override
    public RequestContext apply(RequestContext requestContext) {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
        transactionRequestDto.setAmount(requestContext.getProductDto().getPrice());
        requestContext.setTransactionRequestDto(transactionRequestDto);
        return requestContext;
    }
}
