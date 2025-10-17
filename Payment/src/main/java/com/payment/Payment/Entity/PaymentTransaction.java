package com.payment.Payment.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.payment.Payment.Enum.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transactionID", nullable = false, length = 50)
    private String transactionID;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "unitPriceID", nullable = false)
    @JsonBackReference
    private UnitPrice unitPrice;

    @NotNull
    @Column(name = "tableID", nullable = false, length = 50)
    private String tableID;

    @NotNull
    @Column(name = "matchID", nullable = false, length = 50)
    private Integer matchID;

    @NotNull
    @Column(name = "customerID", nullable = false, length = 50)
    private String customerID;

    @NotNull
    @Column(name = "creatorID", nullable = false, length = 50)
    private String creatorID; //staffID

    @NotNull
    @Column(name = "creatorName", nullable = false, length = 50)
    private String creatorName; //staffName

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tableType", nullable = false, length = 10)
    private TableType tableType;

    @NotNull
    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "endTime", nullable = false)
    private LocalDateTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMethod", nullable = false)
    private PaymentMethod paymentMethod; // VNPAY / MOMO / CASH

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private PaymentStatus status;        // PENDING / SUCCESS / FAILED

    @Column(name = "price", length = 10)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currencyCode", length = 10)
    private CurencyCode currencyCode; // VND, USD

    @Column(name = "amount", length = 10)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "unitOfMeasure", length = 10)
    private UnitOfMeasure unitOfMeasure; //hour, minute, second

    @Column(name = "modifierID", length = 50)
    private String modifierID;

    @NotNull
    @Column(name = "createAt")
    private LocalDateTime createdAt;

    @Column(name = "updateAt")
    private LocalDateTime updatedAt;
}