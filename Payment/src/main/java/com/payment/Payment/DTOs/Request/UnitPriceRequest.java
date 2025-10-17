package com.payment.Payment.DTOs.Request;

import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;
import com.payment.Payment.Enum.CurencyCode;
import com.payment.Payment.Enum.TableType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UnitPriceRequest {
    private String storeID;
    private TableType tableType;
    private Double basePrice;
    private CurencyCode currencyCode; // VND, USD
}
