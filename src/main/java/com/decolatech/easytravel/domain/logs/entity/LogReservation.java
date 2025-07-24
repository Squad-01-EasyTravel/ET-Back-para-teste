package com.decolatech.easytravel.domain.logs.entity;

import com.decolatech.easytravel.domain.booking.entity.Reservation;
import com.decolatech.easytravel.domain.booking.enums.ReservationStatus;
import com.decolatech.easytravel.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LOG_RESERVATIONS")
@Getter
@Setter
@ToString(exclude = {"reservation", "user"})
@NoArgsConstructor
@AllArgsConstructor
public class LogReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LOG_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime logDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RESERVATION", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER")
    private User user;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NEW_STATUS", nullable = false)
    private ReservationStatus newStatus;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "OLD_STATUS", nullable = false)
    private ReservationStatus oldStatus;
}
