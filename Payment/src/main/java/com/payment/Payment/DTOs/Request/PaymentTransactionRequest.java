package com.payment.Payment.DTOs.Request;

import com.payment.Payment.Entity.UnitPrice;
import com.payment.Payment.Enum.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentTransactionRequest {
    private String storeID;
    private String tableID;
    private Integer matchID;
    private String customerID;
    private String creatorID; //staffID
    private String creatorName; //staffName
    private TableType tableType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
