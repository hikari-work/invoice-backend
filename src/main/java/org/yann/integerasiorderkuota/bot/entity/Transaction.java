package org.yann.integerasiorderkuota.bot.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction implements Draft{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    private int amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "message_id")
    private String messageId;
}
