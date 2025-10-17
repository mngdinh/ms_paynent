package com.payment.Payment.Service.Interface;

import com.payment.Payment.DTOs.Request.UnitPriceRequest;
import com.payment.Payment.DTOs.Respone.UnitPriceResponse;

public interface IUnitPriceService {
    UnitPriceResponse createUnitPrice(UnitPriceRequest unitPriceRequest);
    UnitPriceResponse findUnitPrice(Integer unitPriceID);
}
