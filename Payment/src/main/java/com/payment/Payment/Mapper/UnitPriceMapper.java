package com.payment.Payment.Mapper;

import com.payment.Payment.DTOs.Request.UnitPriceRequest;
import com.payment.Payment.DTOs.Respone.UnitPriceResponse;
import com.payment.Payment.Entity.UnitPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnitPriceMapper {
    UnitPriceResponse toUnitPriceResponse(UnitPrice unitPrice);
    UnitPrice toUnitPrice(UnitPriceRequest unitPriceRequest);
}
