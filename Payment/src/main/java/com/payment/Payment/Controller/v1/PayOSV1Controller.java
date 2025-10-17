package com.payment.Payment.Controller.v1;

import com.payment.Payment.DTOs.Request.PayOSRequest;
import com.payment.Payment.DTOs.Request.PayOSWebhookRequest;
import com.payment.Payment.Service.PayOSService;
import com.payment.Payment.Service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;

@Slf4j
@Tag(name = "PayOS V1", description = "PayOS API")
@RestController
@RequestMapping("v1/payos")
@CrossOrigin(origins = "http://localhost:5173")
public class PayOSV1Controller {

    @Autowired
    private PayOSService payosService;
    @Autowired
    private PaymentService paymentService;

    public PayOSV1Controller(PayOSService payosService) {
        this.payosService = payosService;
    }

    @PostMapping("/create-payment-link")
    public CheckoutResponseData createPayment(@RequestBody PayOSRequest request) throws Exception {
        return payosService.createPaymentLink(
                request.getProductName(),
                request.getQuantity(),
                request.getPrice()
        );
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody PayOSWebhookRequest payload) {
        // ✅ Xác thực chữ ký (nếu có cấu hình secret key)
        // ✅ Cập nhật trạng thái giao dịch trong DB

        paymentService.updatePaymentStatus(payload);
        return ResponseEntity.ok("OK");
    }

}
