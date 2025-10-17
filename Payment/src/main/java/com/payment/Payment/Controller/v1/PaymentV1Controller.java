package com.payment.Payment.Controller.v1;

import com.payment.Payment.DTOs.Request.PageableRequestDto;
import com.payment.Payment.DTOs.Request.PaymentTransactionRequest;
import com.payment.Payment.DTOs.Respone.PageableResponseDto;
import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;
import com.payment.Payment.DTOs.Respone.ResponseObject;
import com.payment.Payment.DTOs.Respone.UnitPriceResponse;
import com.payment.Payment.Entity.PaymentTransaction;
import com.payment.Payment.Entity.UnitPrice;
import com.payment.Payment.Enum.PaymentStatus;
import com.payment.Payment.Mapper.PaymentMapper;
import com.payment.Payment.Service.PayOSService;
import com.payment.Payment.Service.PaymentService;
import com.payment.Payment.Service.UnitPriceService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "Payment V1", description = "Payment API")
@RestController
@RequestMapping("v1/payments")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentV1Controller {

    private final PaymentService paymentService;
    private final UnitPriceService unitPriceService;
    private final PayOSService payOSService;
    private final PaymentMapper paymentMapper;

    @PostMapping
    public ResponseObject createPaymentTransaction(@RequestBody PaymentTransactionRequest request) throws Exception {
        UnitPrice rs = unitPriceService.findByStoreAndTableType(request.getStoreID(), request.getTableType());
        PaymentTransactionResponse response = paymentService.parsePaymentTransaction(request, rs);
        PaymentTransaction p = paymentMapper.toPayment(response);
        p.setUnitPrice(rs);
        //call PayOS
        CheckoutResponseData checkout = payOSService.createPaymentLink(
                String.valueOf(response.getTableType()),
                1,
                response.getPrice(),
                "Thanh toán trận đấu " + String.valueOf(response.getMatchID())
        );
        response.setBin(checkout.getBin());
        response.setAccountNumber(checkout.getAccountNumber());
        response.setAccountName(checkout.getAccountName());
        response.setDescription(checkout.getDescription());
        response.setExpiredAt(checkout.getExpiredAt());
        response.setCheckoutUrl(checkout.getCheckoutUrl());
        response.setQrCode(checkout.getQrCode());

        response.setOrderCode(checkout.getOrderCode());
        response.setPaymentLinkId(checkout.getPaymentLinkId());
        p.setOrderCode(checkout.getOrderCode());
        p.setPaymentLinkId(checkout.getPaymentLinkId());

        paymentService.savePaymentTransaction(p);

        return ResponseObject.builder()
                .status(1000)
                .message("Payment Transaction Created")
                .data(response)
                .build();
    }

    @GetMapping
    public ResponseObject getPayment(
            @Parameter(description = "Query type: all, byStore, byId",
                    required = true,
                    schema = @Schema(
                            allowableValues = {"all", "byUnitPrice", "byTable", "byCustomer", "byTableType", "byMatch"}
                    ))
            @RequestParam(defaultValue = "all") String queryType,

            @Parameter(description = "Unit Price ID (required for queryType=byUnitPrice)")
            @RequestParam(required = false) Integer unitTypeID,

            @Parameter(description = "Table ID (required for queryType=byTable)")
            @RequestParam(required = false) String tableID,

            @Parameter(description = "Customer ID (required for queryType=byCustomer)")
            @RequestParam(required = false) String customerID,

            @Parameter(description = "Table Type",
                    required = false,
                    schema = @Schema(
                            allowableValues = {"null", "Pool", "Carom", "Snooker"}
                    ))
            @RequestParam(required = false, defaultValue = "null") String tableType,

            @Parameter(description = "Match ID (required for queryType=byMatch)")
            @RequestParam(required = false) Integer matchID,

            @Parameter(description = "Page number (1-based)", required = true)
            @RequestParam(required = false, defaultValue = "1") Integer page,

            @Parameter(description = "Page size", required = true)
            @RequestParam(required = false, defaultValue = "10") Integer size,

            @Parameter(description = "Sort By",
                    required = true,
                    schema = @Schema(
                            allowableValues = {"unitPriceID", "storeID"}
                    ))
            @RequestParam(defaultValue = "unitPriceID") String sortBy,

            @Parameter(description = "Sort Direction",
                    required = true,
                    schema = @Schema(
                            allowableValues = {"desc", "asc"}
                    ))
            @RequestParam(required = true, defaultValue = "desc") String sortDirection
    ) {

        PageableRequestDto req = PageableRequestDto.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        Map<String, Object> filters = new HashMap<>();
        filters.put("queryType", queryType);
        if (unitTypeID != null) filters.put("unitTypeID", unitTypeID);
        if (matchID != null) filters.put("matchID", matchID);
        if ( tableType!= null && !"null".equals(tableType)) filters.put("tableType", tableType);
        if ( tableID!= null && !"null".equals(tableID)) filters.put("tableID", tableID);
        if ( customerID!= null && !"null".equals(customerID)) filters.put("customerID", customerID);

        PageableResponseDto<UnitPriceResponse> p = unitPriceService.getAll(req, filters);

        return ResponseObject.builder()
                .status(1000)
                .message("Data")
                .data(p)
                .build();
    }

    @PutMapping("/status")
    public ResponseObject updatePaymentTransactionStatus(
            @RequestParam(required = true) String transactionID,
            @RequestParam(required = true)PaymentStatus paymentStatus
            ) {
        return ResponseObject.builder()
                .status(1000)
                .message("Payment Transaction Updated")
                .data(paymentService.updatePaymentStatus(transactionID, paymentStatus))
                .build();
    }

    @PutMapping("/searching")
    public ResponseObject searchPaymentTransactions(@RequestParam long orderCode, @RequestParam String paymentLinkId){
        return ResponseObject.builder()
                .status(1000)
                .message("Payment Transaction Searched")
                .data(paymentService.findByOrderCodeAndPaymentLinkId(orderCode, paymentLinkId))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject deletePaymentTransaction(@PathVariable String id) {
        return ResponseObject.builder()
                .status(1000)
                .message("Payment Transaction Deleted")
                .data(paymentService.deletePaymentTransaction(id))
                .build();
    }

}
