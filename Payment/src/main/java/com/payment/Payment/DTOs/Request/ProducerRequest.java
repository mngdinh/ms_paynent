package com.payment.Payment.DTOs.Request;

import com.payment.Payment.Enum.KafkaCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerRequest {
    private KafkaCode code;
    private String tableID;
    private Object data;

}
