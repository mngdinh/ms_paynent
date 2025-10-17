package com.payment.Payment.DTOs.Respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.Payment.Enum.CurencyCode;
import com.payment.Payment.Enum.TableType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitPriceResponse {
    private Integer unitPriceID;
    private String storeID;
    private TableType tableType;
    private Double basePrice;
    private String baseUnit;
    private CurencyCode currencyCode; // VND, USD
    private List<PaymentTransactionResponse> paymentTransactions;
}
