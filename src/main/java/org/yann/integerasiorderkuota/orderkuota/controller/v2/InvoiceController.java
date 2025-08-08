package org.yann.integerasiorderkuota.orderkuota.controller.v2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yann.integerasiorderkuota.orderkuota.dto.InvoiceRequest;
import org.yann.integerasiorderkuota.orderkuota.entity.Invoice;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.service.InvoiceService;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

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
        Invoice invoice = new Invoice(invoiceRequest, userDetailsByUUID.getUsername(), authorization);
        Invoice invoice1 = invoiceService.saveInvoice(invoice);
        return ResponseEntity.ok(invoice1);
    }
}
