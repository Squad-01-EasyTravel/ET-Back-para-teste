package com.decolatech.easytravel.domain.booking.repository;

import com.decolatech.easytravel.domain.booking.entity.Reservation;
import com.decolatech.easytravel.domain.booking.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // Buscar por usuário
    List<Reservation> findByUserId(Integer userId);

    // Buscar por bundle
    List<Reservation> findByBundleId(Integer bundleId);

    // Buscar por status
    List<Reservation> findByReservStatus(ReservationStatus status);

    // Buscar por usuário e status
    List<Reservation> findByUserIdAndReservStatus(Integer userId, ReservationStatus status);

    // Buscar reservas por período
    @Query("SELECT r FROM Reservation r WHERE r.reservDate BETWEEN :startDate AND :endDate")
    List<Reservation> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Buscar reservas ativas de um usuário
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.reservStatus IN (:statuses)")
    List<Reservation> findActiveReservationsByUser(@Param("userId") Integer userId, @Param("statuses") List<ReservationStatus> statuses);

    // Contar reservas por bundle
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.bundle.id = :bundleId")
    Long countReservationsByBundle(@Param("bundleId") Integer bundleId);

    // Verificar se usuário já tem reserva para um bundle
    boolean existsByUserIdAndBundleId(Integer userId, Integer bundleId);
}
