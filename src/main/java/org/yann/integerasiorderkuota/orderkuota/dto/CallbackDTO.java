package org.yann.integerasiorderkuota.orderkuota.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.entity.InvoiceStatus;

import java.time.LocalDateTime;

@Data
public class CallbackDTO {

    private String id;
    private InvoiceStatus status;
    private Long amount;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private String note;
    @JsonProperty("paid_at")
    private Long paidAt;

    public CallbackDTO(Invoice invoice) {
        this.id = invoice.getId();
        this.status = invoice.getStatus();
        this.amount = invoice.getAmount();
        this.createdAt = invoice.getCreatedAt();
        this.note = invoice.getNotes();
        if (invoice.getPaidAt() != null) {
            this.paidAt = invoice.getPaidAt();
        } else {
            this.paidAt = null;
        }
    }

}
