package com.decolatech.easytravel.domain.booking.repository;

import com.decolatech.easytravel.domain.booking.entity.TravelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelHistoryRepository extends JpaRepository<TravelHistory, Integer> {

    // Buscar por payment
    List<TravelHistory> findByPaymentId(Integer paymentId);

    // Buscar históricos por valor do pagamento
    @Query("SELECT th FROM TravelHistory th WHERE th.payment.totPrice BETWEEN :minPrice AND :maxPrice")
    List<TravelHistory> findByPaymentRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}
