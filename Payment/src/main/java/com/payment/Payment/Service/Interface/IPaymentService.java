package com.payment.Payment.Service.Interface;

import com.payment.Payment.DTOs.Request.PaymentTransactionRequest;
import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;

public interface IPaymentService {
    PaymentTransactionResponse createPaymentTransaction(PaymentTransactionRequest paymentTransactionRequest);
}
