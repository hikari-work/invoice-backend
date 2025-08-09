package org.yann.integerasiorderkuota.orderkuota.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.entity.InvoiceStatus;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.repository.InvoiceRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);
    @Value("${application.config.random.reff.id}")
    private boolean randomReffId;

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
        if (randomReffId) {
            for (int i = 0; i < 10; i++) {
                long total = new Random().nextInt(100) + amount;
                if (!isAmountIsExist(total)) {
                    log.info("Generated unique amount: {}", total);
                    return total;
                }
            }
            throw new IllegalStateException("Tidak dapat menemukan jumlah yang unik");
        }
        log.info("Using provided amount: {}", amount);
        return invoiceRepository.findNextAvailableAmount(amount);
    }

    private boolean isAmountIsExist(Long amount) {
        return invoiceRepository.existsByAmountAndStatus(amount, InvoiceStatus.PENDING);
    }

    @Transactional
    public void updateInvoiceStatus(String id, InvoiceStatus status, boolean isPaid) {
        invoiceRepository.findById(id).ifPresent(invoice -> {
            invoice.setStatus(status);
            invoice.setPaidAt(System.currentTimeMillis());
            invoiceRepository.save(invoice);
        });
    }
    @Transactional
    public void updateInvoicesToPaid(Collection<String> ids) {
        invoiceRepository.updateInvoicesToPaid(ids, System.currentTimeMillis());
    }

}
