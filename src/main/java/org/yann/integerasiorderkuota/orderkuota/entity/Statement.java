package org.yann.integerasiorderkuota.orderkuota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "statements")
public class Statement {

    @Id
    private Long id;

    private String username;

    private Long debet;

    private Long kredit;

    private String keterangan;

    private String status;

    @Enumerated(EnumType.STRING)
    @JsonProperty("statement_status")
    private StatementStatus statementStatus;

    @Column(name = "transfer_time")
    @JsonProperty("transfer_time")
    private LocalDateTime transferTime;

    public Statement(SettlementDTO.QrisTransaction transaction, String username) {
        this.id = transaction.getId();
        this.username = username;
        this.debet = Long.parseLong(transaction.getDebet().replace(".", ""));
        this.kredit = Long.parseLong(transaction.getKredit().replace(".", ""));
        this.keterangan = transaction.getKeterangan();
        this.status = transaction.getStatus();
        this.statementStatus = StatementStatus.NOT_CLAIMED;
        this.transferTime = LocalDateTime.parse(transaction.getTanggal(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }


}
