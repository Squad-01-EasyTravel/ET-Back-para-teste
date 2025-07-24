package com.decolatech.easytravel.domain.logs.dto;

import com.decolatech.easytravel.domain.booking.enums.PaymentMethod;
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
@Schema(description = "DTO para transferência de dados de log de pagamento")
public class LogPaymentDTO {

    @Schema(hidden = true)
    private Integer id;

    @Schema(example = "2023-07-23T10:15:30")
    @NotNull(message = "Data do log é obrigatória")
    private LocalDateTime logDate;

    @Schema(example = "3001")
    @NotNull(message = "ID do pagamento é obrigatório")
    private Integer paymentId;

    @Schema(example = "501")
    private Integer userId;

    @Schema(example = "CREDIT_CARD")
    private PaymentMethod newMethod;

    @Schema(example = "1500.00")
    private Double newTotal;

    @Schema(example = "PIX")
    private PaymentMethod oldMethod;

    @Schema(example = "1200.00")
    private Double oldTotal;

    @Schema(example = "João Silva")
    private String userName;
}
