package org.yann.integerasiorderkuota.orderkuota.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yann.integerasiorderkuota.orderkuota.entity.Statement;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
}
