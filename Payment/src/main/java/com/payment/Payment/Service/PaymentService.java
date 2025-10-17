package com.payment.Payment.Service;

import com.payment.Payment.DTOs.Request.PayOSWebhookRequest;
import com.payment.Payment.DTOs.Request.PaymentTransactionRequest;
import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;
import com.payment.Payment.DTOs.Respone.UnitPriceResponse;
import com.payment.Payment.Entity.PaymentTransaction;
import com.payment.Payment.Entity.UnitPrice;
import com.payment.Payment.Enum.*;
import com.payment.Payment.Exception.AppException;
import com.payment.Payment.Exception.ErrorCode;
import com.payment.Payment.Mapper.PaymentMapper;
import com.payment.Payment.Repo.PaymentTransactionRepo;
import com.payment.Payment.Service.Filter.BaseSpecificationService;
import com.payment.Payment.Service.Interface.IPaymentService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService extends BaseSpecificationService<PaymentTransaction, PaymentTransactionResponse>
        implements IPaymentService {

    private final PaymentTransactionRepo paymentTransactionRepo;
    private final PaymentMapper paymentMapper;

    @Override
    protected JpaSpecificationExecutor<PaymentTransaction> getRepository() {
        return paymentTransactionRepo;
    }

    @Override
    protected Function<PaymentTransaction, PaymentTransactionResponse> getMapper() {
        return paymentMapper::toPaymentTransactionResponse;
    }

    @Override
    protected Specification<PaymentTransaction> buildSpecification(Map<String, Object> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();




            String queryType = (String) filters.get("queryType");
            String tableID = (String) filters.get("tableID");
            String customerID = (String) filters.get("customerID");
            String tableType = (String) filters.get("tableType");
            Integer matchID = (Integer) filters.get("matchID");
            Integer unitTypeID = (Integer) filters.get("unitTypeID");

            if ("byUnitPrice".equals(queryType) && unitTypeID != null) {
                predicates.add(cb.equal(root.get("unitPrice").get("unitPriceID"), unitTypeID));
            }
            if ("byMatch".equals(queryType) && matchID != null) {
                predicates.add(cb.equal(root.get("matchID"), matchID));
            }
            if ("byTable".equals(queryType) && !tableID.isEmpty()) {
                predicates.add(cb.equal(root.get("tableID"), tableID));
            }
            if ("byCustomer".equals(queryType) && !customerID.isEmpty()) {
                predicates.add(cb.equal(root.get("customerID"), customerID ));
            }
            if ("byTableType".equals(queryType) && !"null".equals(tableType)) {
                predicates.add(cb.equal(root.get("tableType"), tableType ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


//    private String tableID;
//    private Integer matchID;
//    private String customerID;
//    private String creatorID; //staffID
//    private String creatorName; //staffName
//    private TableType tableType;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;

//    private Integer unitPriceID;
//    private PaymentMethod paymentMethod; // VNPAY / MOMO / CASH
//    private Double price;
//    private CurencyCode currencyCode; // VND, USD
//    private Double amount;
//    private UnitOfMeasure unitOfMeasure; //hour, minute, second
//    private LocalDateTime createdAt;


//    private PaymentStatus status;        // PENDING / SUCCESS / FAILED
    //    private String modifierID;
    //    private LocalDateTime updatedAt;

    @Override
    public PaymentTransactionResponse createPaymentTransaction(PaymentTransactionRequest request, UnitPrice rs) {
        PaymentTransaction p = paymentMapper.toPaymentTransaction(request);
        p.setUnitPrice(rs);
        p.setPaymentMethod(PaymentMethod.VNPAY);
        p.setStatus(PaymentStatus.PENDING);

        //giá tiền mỗi giờ chơi của từng loại bàn theo mỗi store
        Double basePrice = rs.getBasePrice();
        //lay tg đánh
        Duration duration = Duration.between(request.getStartTime(), request.getEndTime());

        //set amount va unitOfMeasure theo hour hoac minute
        long minutes = duration.toMinutes();
        if (minutes > 60) {
            p.setUnitOfMeasure(UnitOfMeasure.hour);
            p.setAmount(Double.parseDouble(String.valueOf(minutes)) / 60);
        } else {
            p.setUnitOfMeasure(UnitOfMeasure.minute);
            p.setAmount(Double.parseDouble(String.valueOf(minutes)));
        }
        //set price
        p.setPrice(rs.getBasePrice() * duration.getSeconds() / 3600); //theo hour
        p.setCreatedAt(LocalDateTime.now());
        //set currency
        p.setCurrencyCode(CurencyCode.VND);

        return paymentMapper.toPaymentTransactionResponse(paymentTransactionRepo.save(p));
    }

    @Override
    public PaymentTransactionResponse updatePaymentStatus(String transactionID, PaymentStatus paymentStatus) {
        PaymentTransaction p = paymentTransactionRepo.findById(transactionID)
                .orElseThrow(() -> new AppException(ErrorCode.NULL_RECORD));
        p.setStatus(paymentStatus);
        p.setUpdatedAt(LocalDateTime.now());
        return paymentMapper.toPaymentTransactionResponse(paymentTransactionRepo.save(p));
    }

    @Override
    public PaymentTransactionResponse updatePaymentStatus(PayOSWebhookRequest request) {
        log.info(request.toString());
        return null;
    }

    @Override
    public boolean deletePaymentTransaction(String transactionID) {
        PaymentTransaction p = paymentTransactionRepo.findById(transactionID)
                .orElseThrow(() -> new AppException(ErrorCode.NULL_RECORD));
        paymentTransactionRepo.delete(p);
        return true;
    }


}
