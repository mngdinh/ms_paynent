package com.payment.Payment.Service.Interface;

import com.payment.Payment.DTOs.Request.UnitPriceRequest;
import com.payment.Payment.DTOs.Respone.UnitPriceResponse;
import com.payment.Payment.Entity.UnitPrice;
import com.payment.Payment.Enum.TableType;

public interface IUnitPriceService {
    UnitPriceResponse createUnitPrice(UnitPriceRequest unitPriceRequest);
    UnitPriceResponse findUnitPrice(Integer unitPriceID);
    UnitPrice findByStoreAndTableType(String storeID, TableType tableType);
    UnitPriceResponse updateUnitPrice(UnitPriceRequest unitPriceRequest);
    boolean deleteUnitPrice(Integer unitPriceID);
}
