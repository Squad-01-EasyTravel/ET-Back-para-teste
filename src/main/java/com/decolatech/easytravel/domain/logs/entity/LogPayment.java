package com.decolatech.easytravel.domain.logs.entity;

import com.decolatech.easytravel.domain.booking.entity.Payment;
import com.decolatech.easytravel.domain.booking.enums.PaymentMethod;
import com.decolatech.easytravel.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LOG_PAYMENTS")
@Getter
@Setter
@ToString(exclude = {"payment", "user"})
@NoArgsConstructor
@AllArgsConstructor
public class LogPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LOG_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime logDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PAYMENT", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER")
    private User user;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NEW_METHOD")
    private PaymentMethod newMethod;

    @Column(name = "NEW_TOTAL")
    private Double newTotal;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "OLD_METHOD")
    private PaymentMethod oldMethod;

    @Column(name = "OLD_TOTAL")
    private Double oldTotal;
}
