package com.payment.Payment.DTOs.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayOSRequest {
    private String productName;
    private int quantity;
    private int price;
}
