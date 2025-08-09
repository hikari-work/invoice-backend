package org.yann.integerasiorderkuota.orderkuota.controller.v2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yann.integerasiorderkuota.orderkuota.dto.InvoiceRequest;
import org.yann.integerasiorderkuota.orderkuota.dto.InvoiceResponse;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.service.InvoiceService;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

import java.net.URI;

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
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest invoiceRequest,
                                           @RequestHeader(value = "Authorization", required = true) String authorization) {
        User userDetailsByUUID = userService.getUserDetailsByUUID(authorization.replace("Bearer ", ""));
        Invoice invoice = new Invoice(invoiceRequest, userDetailsByUUID.getUsername());
        Invoice invoice1 = invoiceService.saveInvoice(invoice, userDetailsByUUID);
        InvoiceResponse response = new InvoiceResponse(invoice1);
        URI location = URI.create("/api/v2/invoices/details/" + invoice1.getId());
        return ResponseEntity.created(location).body(response);
    }
    @GetMapping("/details/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceDetails(@PathVariable("id") String invoice,
                                                             @RequestHeader(value = "Authorization", required = true) String authorization) {
        User userDetailsByUUID = userService.getUserDetailsByUUID(authorization.replace("Bearer ", ""));
        Invoice invoiceFound = invoiceService.getById(invoice).orElseThrow(() -> new IllegalArgumentException("Invoice Not Found"));
        if (!userDetailsByUUID.getUsername().equals(invoiceFound.getUsername())) {
            throw new IllegalArgumentException("Invoice Not Found");
        }
        InvoiceResponse response = new InvoiceResponse(invoiceFound);
        return ResponseEntity.ok(response);
    }

}
