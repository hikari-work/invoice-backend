package org.yann.integerasiorderkuota.orderkuota.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.entity.InvoiceStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    Page<Invoice> getInvoiceById(String id, Pageable pageable);

    Page<Invoice> findByIdAndCreatedAtBetween(String id, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore, Pageable pageable);

    Page<Invoice> getInvoiceByIdAndStatus(String id, InvoiceStatus status, Pageable pageable);

    Page<Invoice> getInvoiceByUsernameAndCreatedAt(String username, LocalDateTime createdAt, Pageable pageable);

    boolean existsByAmountAndStatus(Long amount, InvoiceStatus status);

    List<Invoice> getInvoiceByStatus(InvoiceStatus status);


    @Query("SELECT COALESCE(MAX(i.amount), :amount - 1) + 1 FROM Invoice i WHERE i.status = 'PENDING'")
    Long findNextAvailableAmount(@Param("amount") Long amount);
    @Modifying
    @Query("UPDATE Invoice i " +
            "SET i.status = 'PAID', i.paidAt = :paidAt " +
            "WHERE i.id IN :ids")
    void updateInvoicesToPaid(@Param("ids") Collection<String> ids,
                              @Param("paidAt") Long paidAt);




}
