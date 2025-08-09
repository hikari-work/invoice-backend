package org.yann.integerasiorderkuota.orderkuota.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.entity.InvoiceStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    Page<Invoice> getInvoiceById(String id, Pageable pageable);

    Page<Invoice> findByIdAndCreatedAtBetween(String id, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore, Pageable pageable);

    Page<Invoice> getInvoiceByIdAndStatus(String id, InvoiceStatus status, Pageable pageable);

    Page<Invoice> getInvoiceByUsernameAndCreatedAt(String username, LocalDateTime createdAt, Pageable pageable);

    boolean existsByAmountAndStatus(Long amount, InvoiceStatus status);
}
