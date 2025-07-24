package com.decolatech.easytravel.domain.booking.service;

import com.decolatech.easytravel.domain.booking.dto.PaymentDTO;
import com.decolatech.easytravel.domain.booking.entity.Payment;
import com.decolatech.easytravel.domain.booking.entity.Reservation;
import com.decolatech.easytravel.domain.booking.enums.PaymentMethod;
import com.decolatech.easytravel.domain.booking.repository.PaymentRepository;
import com.decolatech.easytravel.domain.booking.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO getPaymentById(Integer id) {
        return paymentRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
    }

    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Reservation reservation = reservationRepository.findById(paymentDTO.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Payment payment = convertToEntity(paymentDTO);
        payment.setReservation(reservation);
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        return convertToDTO(savedPayment);
    }

    public PaymentDTO updatePayment(Integer id, PaymentDTO paymentDTO) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        Reservation reservation = reservationRepository.findById(paymentDTO.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        boolean updated = false;
        if (!existingPayment.getPaymentMethod().equals(paymentDTO.getPaymentMethod())) updated = true;
        if (!existingPayment.getTotPrice().equals(paymentDTO.getTotPrice())) updated = true;
        if (!existingPayment.getReservation().getId().equals(reservation.getId())) updated = true;

        if (!updated) {
            throw new IllegalArgumentException("Nenhuma alteração foi detectada nos dados do pagamento");
        }

        existingPayment.setPaymentMethod(paymentDTO.getPaymentMethod());
        existingPayment.setTotPrice(paymentDTO.getTotPrice());
        existingPayment.setReservation(reservation);

        Payment savedPayment = paymentRepository.save(existingPayment);
        return convertToDTO(savedPayment);
    }

    public void deletePayment(Integer id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Pagamento não encontrado");
        }
        paymentRepository.deleteById(id);
    }

    public List<PaymentDTO> getPaymentsByReservation(Integer reservationId) {
        return paymentRepository.findByReservationId(reservationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByMethod(PaymentMethod method) {
        return paymentRepository.findByPaymentMethod(method).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateRange(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Double getTotalPaymentsByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.getTotalPaymentsByPeriod(startDate, endDate);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setTotPrice(payment.getTotPrice());
        dto.setReservationId(payment.getReservation().getId());
        return dto;
    }

    private Payment convertToEntity(PaymentDTO dto) {
        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTotPrice(dto.getTotPrice());
        return payment;
    }
}
