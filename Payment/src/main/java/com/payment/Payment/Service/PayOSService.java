package com.payment.Payment.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import java.util.*;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;


@Service
public class PayOSService {

    private final PayOS payOS;
    private final String domain;
    private final String checksumKey;

    @Value("${app.backend.url}")
    private String backendUrl;

    public PayOSService(
            @Value("${payos.client-id}") String clientId,
            @Value("${payos.api-key}") String apiKey,
            @Value("${payos.checksum-key}") String checksumKey,
            @Value("${payos.frontend-domain}") String domain) {

        this.payOS = new PayOS(clientId, apiKey, checksumKey);
        this.domain = domain;
        this.checksumKey = checksumKey;
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

    public Boolean isValidData(String transaction, String transactionSignature) {
        try {
            JSONObject jsonObject = new JSONObject(transaction);
            Iterator<String> sortedIt = sortedIterator(jsonObject.keys(), (a, b) -> a.compareTo(b));

            StringBuilder transactionStr = new StringBuilder();
            while (sortedIt.hasNext()) {
                String key = sortedIt.next();
                String value = jsonObject.get(key).toString();
                transactionStr.append(key);
                transactionStr.append('=');
                transactionStr.append(value);
                if (sortedIt.hasNext()) {
                    transactionStr.append('&');
                }
            }

            String signature = new HmacUtils("HmacSHA256", checksumKey).hmacHex(transactionStr.toString());
            return signature.equals(transactionSignature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Iterator<String> sortedIterator(Iterator<?> it, Comparator<String> comparator) {
        List<String> list = new ArrayList<String>();
        while (it.hasNext()) {
            list.add((String) it.next());
        }

        Collections.sort(list, comparator);
        return list.iterator();
    }

}
