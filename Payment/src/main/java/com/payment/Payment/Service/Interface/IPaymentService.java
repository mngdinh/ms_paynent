package com.payment.Payment.Service.Interface;

import com.payment.Payment.DTOs.Request.PayOSWebhookRequest;
import com.payment.Payment.DTOs.Request.PaymentTransactionRequest;
import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;
import com.payment.Payment.DTOs.Respone.UnitPriceResponse;
import com.payment.Payment.Entity.UnitPrice;
import com.payment.Payment.Enum.PaymentStatus;

public interface IPaymentService {
    PaymentTransactionResponse createPaymentTransaction(PaymentTransactionRequest paymentTransactionRequest, UnitPrice rs);
    PaymentTransactionResponse updatePaymentStatus(String transactionID, PaymentStatus paymentStatus);
    PaymentTransactionResponse updatePaymentStatus(PayOSWebhookRequest request);
    boolean deletePaymentTransaction(String transactionID);
}
