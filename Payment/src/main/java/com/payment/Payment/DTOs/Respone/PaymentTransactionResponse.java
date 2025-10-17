package com.payment.Payment.DTOs.Respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.Payment.Entity.UnitPrice;
import com.payment.Payment.Enum.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentTransactionResponse {
    private String transactionID;
    private UnitPrice unitPrice;
    private String tableID;
    private Integer matchID;
    private String customerID;
    private String creatorID; //staffID
    private String creatorName; //staffName
    private TableType tableType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private PaymentMethod paymentMethod; // VNPAY / MOMO / CASH
    private PaymentStatus status;        // PENDING / SUCCESS / FAILED
    private Double price;
    private CurencyCode currencyCode; // VND, USD
    private Double amount;
    private UnitOfMeasure unitOfMeasure; //hour, minute, second
    private String modifierID;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
