package org.yann.integerasiorderkuota.orderkuota.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.entity.InvoiceStatus;

@Data
public class InvoiceResponse {

    private String username;
    private Long amount;
    private InvoiceStatus status;
    private String note;
    @JsonProperty("expires_at")
    private Long expiresAt;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("qris_string")
    private String qrString;

    public InvoiceResponse(Invoice invoice) {
        this.username = invoice.getUsername();
        this.amount = invoice.getAmount();
        this.status = invoice.getStatus();
        this.note = invoice.getNotes();
        this.expiresAt = invoice.getExpiresAt();
        this.createdAt = invoice.getCreatedAt().toString();
        this.qrString = invoice.getQrString();
    }
}
