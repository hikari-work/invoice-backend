package org.yann.integerasiorderkuota.orderkuota.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yann.integerasiorderkuota.orderkuota.entity.Statement;
import org.yann.integerasiorderkuota.orderkuota.entity.StatementStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
    Page<Statement> findByUsername(String username, Pageable pageable);

    List<Statement> findByStatementStatus(StatementStatus statementStatus);

    @Modifying
    @Query("UPDATE Statement s SET s.status = 'CLAIMED' WHERE s.id IN :ids")
    void updateStatementsToClaimed(@Param("ids") Collection<String> ids);



}
