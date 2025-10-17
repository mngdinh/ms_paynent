package com.payment.Payment.Controller.v1;

import com.payment.Payment.DTOs.Request.PayOSRequest;
import com.payment.Payment.DTOs.Request.PayOSWebhookRequest;
import com.payment.Payment.DTOs.Request.WebsocketReq;
import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;
import com.payment.Payment.Enum.WSFCMCode;
import com.payment.Payment.Enum.WebSocketTopic;
import com.payment.Payment.Service.PayOSService;
import com.payment.Payment.Service.PaymentService;
import com.payment.Payment.Service.WebSocketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    @Autowired
    private WebSocketService webSocketService;

    public PayOSV1Controller(PayOSService payosService) {
        this.payosService = payosService;
    }

    @PostMapping("/create-payment-link")
    public CheckoutResponseData createPayment(@RequestBody PayOSRequest request) throws Exception {
        return payosService.createPaymentLink(
                request.getProductName(),
                request.getQuantity(),
                request.getPrice(),
                "Payment Test"
        );
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody PayOSWebhookRequest payload) {
        log.info("Received PayOS Webhook Request: {}", payload);
        PaymentTransactionResponse p = paymentService.updatePaymentStatus(payload);
        webSocketService.sendToWebSocket(
                WebSocketTopic.NOTI_NOTIFICATION.getValue() + p.getTableID(),
                new WebsocketReq(WSFCMCode.PAYMENT, p)
        );
        webSocketService.sendToWebSocket(
                WebSocketTopic.DASHBOARD.getValue(),
                new WebsocketReq(WSFCMCode.PAYMENT, p)
        );
        log.info("send message by websocket to table: {} ", p.getTableID());
        return ResponseEntity.ok("OK");
    }

}
