package com.decolatech.easytravel.domain.booking.repository;

import com.decolatech.easytravel.domain.booking.entity.Payment;
import com.decolatech.easytravel.domain.booking.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Buscar por reserva
    List<Payment> findByReservationId(Integer reservationId);

    // Buscar por método de pagamento
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    // Buscar por data de pagamento
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByPaymentDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Buscar por faixa de valor
    @Query("SELECT p FROM Payment p WHERE p.totPrice BETWEEN :minPrice AND :maxPrice")
    List<Payment> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    // Buscar pagamentos ordenados por data
    List<Payment> findAllByOrderByPaymentDateDesc();

    // Calcular total de pagamentos por período
    @Query("SELECT SUM(p.totPrice) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    Double getTotalPaymentsByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Contar pagamentos por método
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentMethod = :method")
    Long countPaymentsByMethod(@Param("method") PaymentMethod method);
}
