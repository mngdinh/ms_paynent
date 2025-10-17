package com.payment.Payment.Controller.v1;

import com.payment.Payment.DTOs.Request.PaymentTransactionRequest;
import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;
import com.payment.Payment.DTOs.Respone.ResponseObject;
import com.payment.Payment.Service.PaymentService;
import com.payment.Payment.Service.UnitPriceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Payment V1", description = "Payment API")
@RestController
@RequestMapping("v1/payments")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    private final PaymentService paymentService;
    private final UnitPriceService unitPriceService;

    @PostMapping
    public ResponseObject createPaymentTransaction(@RequestBody PaymentTransactionRequest request) {
        Integer unitPriceID = unitPriceService.findUnitPrice(request.getUnitPriceID()).getUnitPriceID();
        PaymentTransactionResponse response = paymentService.createPaymentTransaction(request);
        return ResponseObject.builder()
                .status(1000)
                .message("Payment Transaction Created")
                .data(response)
                .build();
    }

}
