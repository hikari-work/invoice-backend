package org.yann.integerasiorderkuota.orderkuota.controller.v2;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.yann.integerasiorderkuota.orderkuota.dto.InvoiceRequest;
import org.yann.integerasiorderkuota.orderkuota.dto.InvoiceResponse;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.service.InvoiceService;
import org.yann.integerasiorderkuota.orderkuota.service.QrGenerator;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v2/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserService userService;
    private final QrGenerator qrGenerator;

    public InvoiceController(InvoiceService invoiceService, UserService userService, QrGenerator qrGenerator) {
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.qrGenerator = qrGenerator;
    }

    @PostMapping("/create")
    @Async("asyncExecutor")
    public CompletableFuture<ResponseEntity<InvoiceResponse>> createInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest,
                                                   @RequestHeader(value = "Authorization") String authorization) {
        return CompletableFuture
                .supplyAsync(() -> userService.getUserDetailsByUUID(authorization.replace("Bearer ", "")))
                .thenApplyAsync(userDetails -> {
                    Invoice invoice = new Invoice(invoiceRequest, userDetails.getUsername());
                    Invoice saved = invoiceService.saveInvoice(invoice, userDetails);
                    InvoiceResponse response = new InvoiceResponse(saved);
                    URI location = URI.create("/api/v2/invoices/details/" + saved.getId());
                    return ResponseEntity.created(location).body(response);
                });
    }
    @GetMapping("/details/{id}")
    @Async("asyncExecutor")
    public CompletableFuture<ResponseEntity<InvoiceResponse>> getInvoiceDetails(@PathVariable("id") String invoice,
                                                       @RequestHeader(value = "Authorization") String authorization) {
        return CompletableFuture
                .supplyAsync(() -> userService.getUserDetailsByUUID(authorization.replace("Bearer ", "")))
                .thenApplyAsync(user -> {
                    Invoice invoiceFound = invoiceService.getById(invoice).orElseThrow(() -> new IllegalArgumentException("Invoice Not Found"));
                    if (!user.getUsername().equals(invoiceFound.getUsername())) {
                        throw new IllegalArgumentException("Invoice Not Found");
                    }
                    InvoiceResponse response = new InvoiceResponse(invoiceFound);
                    return ResponseEntity.ok(response);
                });

    }
    @GetMapping(value = "/qris/{invoiceId}", produces = MediaType.IMAGE_PNG_VALUE)
    @Async("asyncExecutor")
    public CompletableFuture<ResponseEntity<byte[]>> getQris(@PathVariable("invoiceId") String invoiceId,
                                                             @RequestParam(value = "height") Integer height,
                                                             @RequestParam(value = "width") Integer width) {
        Invoice invoice = invoiceService.getById(invoiceId).orElse(null);
        if (invoice == null) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        return CompletableFuture
                .supplyAsync(() -> qrGenerator.generateQrImage(invoice.getQrString(), invoice.getAmount(), width, height))
                .thenApplyAsync(ResponseEntity::ok);
    }

}
