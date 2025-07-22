package org.yann.integerasiorderkuota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yann.integerasiorderkuota.bot.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
