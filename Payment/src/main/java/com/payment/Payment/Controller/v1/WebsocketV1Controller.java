package com.payment.Payment.Controller.v1;

import com.payment.Payment.DTOs.Request.WebsocketReq;
import com.payment.Payment.DTOs.Respone.ResponseObject;
import com.payment.Payment.Enum.WSFCMCode;
import com.payment.Payment.Enum.WebSocketTopic;
import com.payment.Payment.Service.WebSocketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "WebSocket V1", description = "Web SocketAPI")
@RestController
@RequestMapping("v1/websockets")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebsocketV1Controller {

    WebSocketService webSocketService;

    @MessageMapping("/payment.log")
    public void handleLoggingNotification(String message, @DestinationVariable String tableID) {
        log.info("Received log from table {}: {}", tableID, message);
        webSocketService.sendToWebSocket(
                WebSocketTopic.PAYMENT_SUCCESS.getValue() +  tableID,
                new WebsocketReq(WSFCMCode.PAYMENT_SUCCESS, message)
        );
    }

    @PostMapping("/payment")
    public ResponseObject sendPayment(@RequestParam String tableID, @RequestParam String message) {
        log.info("Send log from destination {} to table {} with message: {}", WebSocketTopic.PAYMENT_SUCCESS.getValue() +  tableID, tableID, message);
        webSocketService.sendToWebSocket(
                WebSocketTopic.PAYMENT_SUCCESS.getValue() +  tableID,
                new WebsocketReq(WSFCMCode.PAYMENT_SUCCESS, message)
        );
        return ResponseObject.builder()
                .status(1000)
                .message("OK")
                .build();
    }

}
