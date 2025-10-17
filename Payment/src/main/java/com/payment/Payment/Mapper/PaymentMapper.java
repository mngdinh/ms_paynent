package com.payment.Payment.Mapper;

import com.payment.Payment.DTOs.Request.PaymentTransactionRequest;
import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;
import com.payment.Payment.Entity.PaymentTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "unitPrice.basePrice", target = "basePrice")
    @Mapping(source = "unitPrice.baseUnit", target = "baseUnit")
    PaymentTransactionResponse toPaymentTransactionResponse(PaymentTransaction paymentTransaction);


    PaymentTransaction toPaymentTransaction(PaymentTransactionRequest paymentTransactionRequest);
}
