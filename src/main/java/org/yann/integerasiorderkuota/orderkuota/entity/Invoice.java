package org.yann.integerasiorderkuota.orderkuota.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.yann.integerasiorderkuota.orderkuota.dto.InvoiceRequest;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private LocalDateTime createdAt;

    private Long expiresAt;

    private Long paidAt;

    private String qrString;

    private boolean isPaid;

    @Nullable
    private String notes;

    public Invoice(InvoiceRequest invoiceRequest, String username, String qrString) {
        this.amount = invoiceRequest.getAmount();
        this.username = username;
        this.status = InvoiceStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = invoiceRequest.getExpiresAt() + System.currentTimeMillis();
        this.paidAt = null;
        this.qrString = qrString;
        this.isPaid = false;
        this.notes = invoiceRequest.getNotes();

    }

}
