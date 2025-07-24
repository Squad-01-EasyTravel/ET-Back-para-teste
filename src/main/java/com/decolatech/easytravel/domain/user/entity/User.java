package com.decolatech.easytravel.domain.user.entity;

import com.decolatech.easytravel.domain.user.enums.UserRole;
import com.decolatech.easytravel.domain.user.enums.UserStatus;
import com.decolatech.easytravel.domain.booking.entity.Reservation;
import com.decolatech.easytravel.domain.logs.entity.LogUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TB_USERS")
@Getter
@Setter
@ToString(exclude = {"userPassword", "reservations"})
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USERNAME", nullable = false, length = 100)
    private String username;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "CPF", nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(name = "PASSPORT", length = 10, unique = true)
    private String passport;

    @Column(name = "USER_PASSWORD", nullable = false)
    private String userPassword;

    @Column(name = "TELEPHONE", nullable = false, length = 18)
    private String telephone;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "USER_STATUS", nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVATED;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "USER_ROLE", nullable = false)
    private UserRole userRole;

    // Relacionamentos
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    // Relacionamentos com logs
    @OneToMany(mappedBy = "userTarget", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LogUser> logEntriesAsTarget;

    @OneToMany(mappedBy = "userAction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LogUser> logEntriesAsAction;
}
