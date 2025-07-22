package org.yann.integerasiorderkuota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yann.integerasiorderkuota.bot.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
