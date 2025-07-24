package com.decolatech.easytravel.domain.logs.repository;

import com.decolatech.easytravel.domain.logs.entity.LogPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogPaymentRepository extends JpaRepository<LogPayment, Integer> {

    // Buscar por pagamento
    List<LogPayment> findByPaymentId(Integer paymentId);

    // Buscar por usuário que fez a alteração
    List<LogPayment> findByUserId(Integer userId);

    // Buscar logs por período
    @Query("SELECT l FROM LogPayment l WHERE l.logDate BETWEEN :startDate AND :endDate")
    List<LogPayment> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Buscar logs ordenados por data
    List<LogPayment> findAllByOrderByLogDateDesc();

    // Contar alterações por pagamento
    @Query("SELECT COUNT(l) FROM LogPayment l WHERE l.payment.id = :paymentId")
    Long countLogsByPayment(@Param("paymentId") Integer paymentId);
}
