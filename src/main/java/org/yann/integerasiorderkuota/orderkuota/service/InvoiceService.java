package org.yann.integerasiorderkuota.orderkuota.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.entity.InvoiceStatus;
import org.yann.integerasiorderkuota.orderkuota.repository.InvoiceRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Page<Invoice> getInvoiceByUser(String userId, int page, int size) {
        return invoiceRepository.getInvoiceById(userId, PageRequest.of(page, size));
    }
    public Page<Invoice> getInvoiceByUserAndStatus(String userId, InvoiceStatus status, int page, int size) {
        return invoiceRepository.getInvoiceByIdAndStatus(userId, status, PageRequest.of(page, size));
    }

    public Page<Invoice> getInvoiceByUserAndDataRange(String userId, String startDate, String endDate, int page, int size) {
        LocalDateTime startTime = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDateTime endTime = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return invoiceRepository.findByIdAndCreatedAtBetween(userId, startTime, endTime, PageRequest.of(page, size));
    }
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }
    public Optional<Invoice> getById(String id) {
        return invoiceRepository.findById(id);
    }
    public Page<Invoice> getInvoiceByUserAndCreatedAt(String userId, LocalDateTime createdAt, int page, int size) {
        return invoiceRepository.getInvoiceByUsernameAndCreatedAt(userId, createdAt, PageRequest.of(page, size));
    }

}
