package com.payment.Payment.Service;

import com.payment.Payment.DTOs.Request.PaymentTransactionRequest;
import com.payment.Payment.DTOs.Respone.PaymentTransactionResponse;
import com.payment.Payment.Entity.PaymentTransaction;
import com.payment.Payment.Mapper.PaymentMapper;
import com.payment.Payment.Repo.PaymentTransactionRepo;
import com.payment.Payment.Service.Filter.BaseSpecificationService;
import com.payment.Payment.Service.Interface.IPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public PaymentTransactionResponse createPaymentTransaction(PaymentTransactionRequest paymentTransactionRequest) {
        return null;
    }


}
