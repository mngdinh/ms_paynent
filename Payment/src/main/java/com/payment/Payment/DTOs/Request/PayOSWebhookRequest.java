package com.payment.Payment.DTOs.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayOSWebhookRequest {
    @JsonProperty("code")
    private String code;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("data")
    private PayOSWebhookDataRequest data;

    @JsonProperty("signature")
    private String signature;
}
