package org.yann.integerasiorderkuota.orderkuota.controller.v2;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.yann.integerasiorderkuota.orderkuota.dto.InvoiceRequest;
import org.yann.integerasiorderkuota.orderkuota.dto.InvoiceResponse;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.service.InvoiceService;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v2/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserService userService;

    public InvoiceController(InvoiceService invoiceService, UserService userService) {
        this.userService = userService;
        this.invoiceService = invoiceService;
    }

    @PostMapping("/create")
    @Async("asyncExecutor")
    public CompletableFuture<ResponseEntity<InvoiceResponse>> createInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest,
                                                   @RequestHeader(value = "Authorization", required = true) String authorization) {
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
                                                       @RequestHeader(value = "Authorization", required = true) String authorization) {
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

}
