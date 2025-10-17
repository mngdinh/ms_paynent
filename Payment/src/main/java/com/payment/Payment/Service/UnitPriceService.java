package com.payment.Payment.Service;

import com.payment.Payment.DTOs.Request.UnitPriceRequest;
import com.payment.Payment.DTOs.Respone.UnitPriceResponse;
import com.payment.Payment.Entity.UnitPrice;
import com.payment.Payment.Enum.BaseUnit;
import com.payment.Payment.Enum.TableType;
import com.payment.Payment.Exception.AppException;
import com.payment.Payment.Exception.ErrorCode;
import com.payment.Payment.Mapper.UnitPriceMapper;
import com.payment.Payment.Repo.UnitPriceRepo;
import com.payment.Payment.Service.Filter.BaseSpecificationService;
import com.payment.Payment.Service.Interface.IUnitPriceService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnitPriceService extends BaseSpecificationService<UnitPrice, UnitPriceResponse> implements IUnitPriceService {

    private final UnitPriceRepo unitPriceRepo;
    private final UnitPriceMapper unitPriceMapper;


    @Override
    protected JpaSpecificationExecutor<UnitPrice> getRepository() {
        return unitPriceRepo;
    }

    @Override
    protected Function<UnitPrice, UnitPriceResponse> getMapper() {
        return unitPriceMapper::toUnitPriceResponse;
    }

    @Override
    protected Specification<UnitPrice> buildSpecification(Map<String, Object> filters) {
        return (root, query,cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            String queryType = (String) filters.get("queryType");
            String storeId = (String) filters.get("storeId");
            String tableType = (String) filters.get("tableType");

            // Nếu queryType là byStore mà storeId trống -> trả về false
            if ("byStore".equals(queryType) && (storeId == null || storeId.isEmpty())) {
                // Trick: tạo predicate luôn sai => không trả về kết quả nào
                return cb.disjunction(); // tương đương WHERE 1=0
            }

            if ("all".equals(queryType)) {
                if (tableType != null && !tableType.isEmpty()) {
                    predicates.add(cb.equal(root.get("tableType"), tableType));
                }
            }
            else if ("byStore".equals(queryType)) {
                predicates.add(cb.equal(root.get("store").get("storeID"), storeId));
                if (tableType != null && !tableType.isEmpty()) {
                    predicates.add(cb.equal(root.get("tableType"), tableType));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    @Transactional
    public UnitPriceResponse createUnitPrice(UnitPriceRequest request) {
        // Kiểm tra xem có record nào trùng không
        UnitPrice existing = unitPriceRepo.findByStoreIDAndTableType(request.getStoreID(), request.getTableType());

        // Nếu tồn tại -> cập nhật field
        String baseUnit = BaseUnit.VND_PER_HOUR.name().replace("_PER_", "/").replace("_", " ");
        if (existing != null) {
            existing.setBasePrice(request.getBasePrice());
            existing.setBaseUnit(baseUnit);
            existing.setCurrencyCode(request.getCurrencyCode());
            // ... cập nhật thêm các field khác nếu cần
            unitPriceRepo.save(existing);
            return unitPriceMapper.toUnitPriceResponse(existing);
        }

        // Nếu chưa tồn tại -> tạo mới
        UnitPrice newUnit = unitPriceMapper.toUnitPrice(request);
        newUnit.setBaseUnit(baseUnit);
        unitPriceRepo.save(newUnit);

        return unitPriceMapper.toUnitPriceResponse(newUnit);
    }

    @Override
    public UnitPriceResponse findUnitPrice(Integer unitPriceID) {
        UnitPrice u = unitPriceRepo.findById(unitPriceID)
                .orElseThrow(() -> new AppException(ErrorCode.NULL_RECORD));
        return unitPriceMapper.toUnitPriceResponse(u);
    }

    @Override
    public UnitPrice findByStoreAndTableType(String storeID,  TableType tableType) {
        UnitPrice unit = unitPriceRepo.findByStoreIDAndTableType(storeID, tableType);
        if (unit == null) throw new AppException(ErrorCode.NULL_RECORD);
        return unit;
    }

    @Override
    public UnitPriceResponse updateUnitPrice(UnitPriceRequest unitPriceRequest) {
        UnitPrice u = findByStoreAndTableType(unitPriceRequest.getStoreID(), unitPriceRequest.getTableType());
        u.setBasePrice(unitPriceRequest.getBasePrice());
        u.setCurrencyCode(unitPriceRequest.getCurrencyCode());
        return unitPriceMapper.toUnitPriceResponse(unitPriceRepo.save(u));
    }

    @Override
    @Transactional
    public boolean deleteUnitPrice(Integer unitPriceID) {
        UnitPrice u = unitPriceRepo.findById(unitPriceID)
                .orElseThrow(() -> new AppException(ErrorCode.NULL_RECORD));
        unitPriceRepo.delete(u);
        return true;
    }


}
