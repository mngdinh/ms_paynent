package com.payment.Payment.DTOs.Request;

import com.payment.Payment.Enum.WSFCMCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketReq {
    private WSFCMCode code;
    private Object data;


}
