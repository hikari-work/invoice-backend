package org.yann.integerasiorderkuota.orderkuota.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InvoiceRequest {

    @JsonProperty("notes")
    @NotBlank
    private String notes;
    @JsonProperty("amount")
    private Long amount;
    @JsonProperty("expires_at")
    private Long expiresAt;
}
