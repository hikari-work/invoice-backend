package org.yann.integerasiorderkuota.orderkuota.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.entity.InvoiceStatus;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.repository.InvoiceRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final QrGenerator qrGenerator;

    public InvoiceService(InvoiceRepository invoiceRepository, QrGenerator qrGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.qrGenerator = qrGenerator;
    }
    public boolean isExistingInvoiceActive(Long amount) {
        return invoiceRepository.existsByAmountAndStatus(amount, InvoiceStatus.PENDING);
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
    @Transactional
    public Invoice saveInvoice(Invoice invoice, User user) {
        invoice.setAmount(amountCalculator(invoice.getAmount()));
        invoice.setQrString(qrGenerator.generateQr(user.getQrisString(),invoice.getAmount()));
        return invoiceRepository.save(invoice);
    }
    public Optional<Invoice> getById(String id) {
        return invoiceRepository.findById(id);
    }

    public List<Invoice> getPendingInvoice() {
        return invoiceRepository.getInvoiceByStatus(InvoiceStatus.PENDING);
    }
    public Page<Invoice> getInvoiceByUserAndCreatedAt(String userId, LocalDateTime createdAt, int page, int size) {
        return invoiceRepository.getInvoiceByUsernameAndCreatedAt(userId, createdAt, PageRequest.of(page, size));
    }
    public Long amountCalculator(Long amount) {
        Long generateAmount = amount;
        while (isExistingInvoiceActive(generateAmount)) {
            generateAmount += 1;
        }
        return generateAmount;
    }
    @Transactional
    public void updateInvoiceStatus(String id, InvoiceStatus status) {
        invoiceRepository.findById(id).ifPresent(invoice -> {
            invoice.setStatus(status);
            invoice.setPaid(true);
            invoice.setPaidAt(System.currentTimeMillis());
            invoiceRepository.save(invoice);
        });
    }

}
