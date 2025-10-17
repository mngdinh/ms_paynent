package com.payment.Payment.Controller.v1;

import com.payment.Payment.DTOs.Respone.ResponseObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "HealthCheck V1", description = "Health Check")
@RestController
@RequestMapping("v1/health")
@CrossOrigin(origins = "http://localhost:5173")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HealthCheck {

    @GetMapping
    public ResponseObject healthCheck() {
        return ResponseObject.builder()
                .status(1000)
                .message("Health Check V1")
                .build();
    }

}
