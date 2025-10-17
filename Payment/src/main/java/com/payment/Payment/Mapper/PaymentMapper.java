package com.payment.Payment.Mapper;

import com.payment.Payment.DTOs.Request.PaymentTransactionRequest;
import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;
import com.payment.Payment.Entity.PaymentTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentTransactionResponse toPaymentTransactionResponse(PaymentTransaction paymentTransaction);
    PaymentTransaction toPaymentTransaction(PaymentTransactionRequest paymentTransactionRequest);
}
