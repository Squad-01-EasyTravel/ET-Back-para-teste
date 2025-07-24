package com.decolatech.easytravel.domain.logs.repository;

import com.decolatech.easytravel.domain.logs.entity.LogReservation;
import com.decolatech.easytravel.domain.booking.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogReservationRepository extends JpaRepository<LogReservation, Integer> {

    // Buscar por reserva
    List<LogReservation> findByReservationId(Integer reservationId);

    // Buscar por usuário que fez a alteração
    List<LogReservation> findByUserId(Integer userId);

    // Buscar por novo status
    List<LogReservation> findByNewStatus(ReservationStatus newStatus);

    // Buscar por status antigo
    List<LogReservation> findByOldStatus(ReservationStatus oldStatus);

    // Buscar logs por período
    @Query("SELECT l FROM LogReservation l WHERE l.logDate BETWEEN :startDate AND :endDate")
    List<LogReservation> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Buscar logs ordenados por data
    List<LogReservation> findAllByOrderByLogDateDesc();

    // Buscar mudanças de status específicas
    List<LogReservation> findByOldStatusAndNewStatus(ReservationStatus oldStatus, ReservationStatus newStatus);

    // Contar alterações por reserva
    @Query("SELECT COUNT(l) FROM LogReservation l WHERE l.reservation.id = :reservationId")
    Long countLogsByReservation(@Param("reservationId") Integer reservationId);
}
