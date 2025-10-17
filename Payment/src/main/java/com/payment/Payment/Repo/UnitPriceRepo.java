package com.payment.Payment.Repo;

import com.payment.Payment.Entity.UnitPrice;
import com.payment.Payment.Enum.TableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UnitPriceRepo extends JpaRepository<UnitPrice, Integer>, JpaSpecificationExecutor<UnitPrice> {
    UnitPrice findByStoreIDAndTableType(String storeID, TableType tableType);
    List<UnitPrice> findByStoreID(String storeID);
}
