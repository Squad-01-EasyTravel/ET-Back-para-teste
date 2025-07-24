package com.decolatech.easytravel.domain.booking.service;

import com.decolatech.easytravel.domain.booking.dto.TravelerDTO;
import com.decolatech.easytravel.domain.booking.entity.Traveler;
import com.decolatech.easytravel.domain.booking.entity.Reservation;
import com.decolatech.easytravel.domain.booking.repository.TravelerRepository;
import com.decolatech.easytravel.domain.booking.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelerService {

    @Autowired
    private TravelerRepository travelerRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<TravelerDTO> getAllTravelers() {
        return travelerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TravelerDTO getTravelerById(Integer id) {
        return travelerRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Viajante não encontrado"));
    }

    public TravelerDTO createTraveler(TravelerDTO travelerDTO) {
        if (travelerDTO.getReservationId() == null) {
            throw new RuntimeException("ID da reserva é obrigatório");
        }

        Reservation reservation = reservationRepository.findById(travelerDTO.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        if (travelerRepository.existsByDocumentNumber(travelerDTO.getDocumentNumber())) {
            throw new RuntimeException("Documento já cadastrado");
        }

        Traveler traveler = convertToEntity(travelerDTO);
        traveler.setReservation(reservation);

        Traveler savedTraveler = travelerRepository.save(traveler);
        return convertToDTO(savedTraveler);
    }

    public TravelerDTO updateTraveler(Integer id, TravelerDTO travelerDTO) {
        Traveler existingTraveler = travelerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viajante não encontrado"));

        boolean updated = false;

        if (travelerDTO.getFullName() != null && !existingTraveler.getFullName().equals(travelerDTO.getFullName())) {
            existingTraveler.setFullName(travelerDTO.getFullName());
            updated = true;
        }

        if (travelerDTO.getDocumentNumber() != null && !existingTraveler.getDocumentNumber().equals(travelerDTO.getDocumentNumber())) {
            existingTraveler.setDocumentNumber(travelerDTO.getDocumentNumber());
            updated = true;
        }

        if (travelerDTO.getDocumentType() != null && !existingTraveler.getDocumentType().equals(travelerDTO.getDocumentType())) {
            existingTraveler.setDocumentType(travelerDTO.getDocumentType());
            updated = true;
        }

        if (travelerDTO.getAge() != null && !existingTraveler.getAge().equals(travelerDTO.getAge())) {
            existingTraveler.setAge(travelerDTO.getAge());
            updated = true;
        }

        if (travelerDTO.getReservationId() != null &&
            !existingTraveler.getReservation().getId().equals(travelerDTO.getReservationId())) {
            Reservation reservation = reservationRepository.findById(travelerDTO.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
            existingTraveler.setReservation(reservation);
            updated = true;
        }

        if (!updated) {
            throw new IllegalArgumentException("Nenhuma alteração foi detectada no viajante");
        }

        Traveler savedTraveler = travelerRepository.save(existingTraveler);
        return convertToDTO(savedTraveler);
    }

    public void deleteTraveler(Integer id) {
        if (!travelerRepository.existsById(id)) {
            throw new RuntimeException("Viajante não encontrado");
        }
        travelerRepository.deleteById(id);
    }

    public List<TravelerDTO> getTravelersByReservation(Integer reservationId) {
        if (reservationId == null) {
            throw new IllegalArgumentException("ID da reserva não pode ser nulo");
        }
        return travelerRepository.findByReservationId(reservationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TravelerDTO> searchTravelersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        return travelerRepository.findByFullNameContainingIgnoreCase(name.trim()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TravelerDTO convertToDTO(Traveler traveler) {
        TravelerDTO dto = new TravelerDTO();
        dto.setId(traveler.getId());
        dto.setFullName(traveler.getFullName());
        dto.setDocumentNumber(traveler.getDocumentNumber());
        dto.setDocumentType(traveler.getDocumentType());
        dto.setAge(traveler.getAge());
        dto.setReservationId(traveler.getReservation() != null ? traveler.getReservation().getId() : null);
        return dto;
    }

    private Traveler convertToEntity(TravelerDTO dto) {
        Traveler traveler = new Traveler();
        traveler.setId(dto.getId());
        traveler.setFullName(dto.getFullName());
        traveler.setDocumentNumber(dto.getDocumentNumber());
        traveler.setDocumentType(dto.getDocumentType());
        traveler.setAge(dto.getAge());
        return traveler;
    }
}
