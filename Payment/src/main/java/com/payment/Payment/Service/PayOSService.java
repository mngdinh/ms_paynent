package com.payment.Payment.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;


@Service
public class PayOSService {

    private final PayOS payOS;
    private final String domain;

    @Value("${app.backend.url}")
    private String backendUrl;

    public PayOSService(
            @Value("${payos.client-id}") String clientId,
            @Value("${payos.api-key}") String apiKey,
            @Value("${payos.checksum-key}") String checksumKey,
            @Value("${payos.frontend-domain}") String domain) {

        this.payOS = new PayOS(clientId, apiKey, checksumKey);
        this.domain = domain;
    }


    public CheckoutResponseData createPaymentLink(String name, int quantity, int price, String dessciption) throws Exception {
        long orderCode = System.currentTimeMillis() / 1000;

        ItemData itemData = ItemData.builder()
                .name(name)
                .quantity(quantity)
                .price(price)
                .build();

        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(price * quantity)
                .description(dessciption)
                .returnUrl(domain + "/success.html")
                .cancelUrl(domain + "/cancel.html")
                .item(itemData)
                .build();

        return payOS.createPaymentLink(paymentData);
    }
}
