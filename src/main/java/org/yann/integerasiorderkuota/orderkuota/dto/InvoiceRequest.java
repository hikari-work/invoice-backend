package org.yann.integerasiorderkuota.orderkuota.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InvoiceRequest {

    @JsonProperty("notes")
    private String notes;
    @JsonProperty("amount")
    private Long amount;
    @JsonProperty("expires_at")
    private Long expiresAt;
}
