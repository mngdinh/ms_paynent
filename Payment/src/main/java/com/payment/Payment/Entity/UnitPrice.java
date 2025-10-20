package com.payment.Payment.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.payment.Payment.Enum.CurencyCode;
import com.payment.Payment.Enum.TableType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unitPriceID", nullable = false)
    private Integer unitPriceID;

    @NotNull
    @Column(name = "storeID", nullable = false, length = 50)
    private String storeID;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tableType", nullable = false, length = 10)
    private TableType tableType;

    @NotNull
    @Column(name = "basePrice", length = 10)
    private Double basePrice;

    @NotNull
    @Column(name = "baseUnit", length = 10)
    private String baseUnit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currencyCode", length = 10)
    private CurencyCode currencyCode; // VND, USD

    @OneToMany(mappedBy = "unitPrice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PaymentTransaction>  paymentTransactions = new ArrayList<>();

}
