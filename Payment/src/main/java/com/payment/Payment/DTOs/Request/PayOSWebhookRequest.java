package com.payment.Payment.DTOs.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayOSWebhookRequest {
    private Long orderCode;
    private Double amount;
    private String description;
    private String status;
    private String paymentLinkId;
    private String accountNumber;
    private String accountName;
}
