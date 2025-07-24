package com.decolatech.easytravel.domain.logs.dto;

import com.decolatech.easytravel.domain.booking.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para transferência de dados de log de reserva")
public class LogReservationDTO {

    @Schema(hidden = true)
    private Integer id;

    @Schema(example = "2023-07-23T10:15:30")
    @NotNull(message = "Data do log é obrigatória")
    private LocalDateTime logDate;

    @Schema(example = "1001")
    @NotNull(message = "ID da reserva é obrigatório")
    private Integer reservationId;

    @Schema(example = "501")
    private Integer userId;

    @Schema(example = "CONFIRMED")
    @NotNull(message = "Novo status é obrigatório")
    private ReservationStatus newStatus;

    @Schema(example = "PENDING")
    @NotNull(message = "Status antigo é obrigatório")
    private ReservationStatus oldStatus;

    @Schema(example = "João Silva")
    private String userName;
}
