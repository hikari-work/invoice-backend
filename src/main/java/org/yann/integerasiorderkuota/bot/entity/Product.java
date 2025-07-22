package org.yann.integerasiorderkuota.bot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product implements Draft{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String name;
    private String description;

    @Lob
    private String image;
    private Long price;

    @OneToMany(mappedBy = "product")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "product")
    private List<Stock> stocks;



}
