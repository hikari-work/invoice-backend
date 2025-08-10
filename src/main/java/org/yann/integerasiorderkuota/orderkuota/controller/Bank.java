package org.yann.integerasiorderkuota.orderkuota.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yann.integerasiorderkuota.orderkuota.client.bank.*;
import org.yann.integerasiorderkuota.orderkuota.exception.RequestParamNotFullFilled;

import java.util.Optional;

@RestController
@RequestMapping("/api/v2/bank")
public class Bank {
    private final BankListRequest bankListRequest;
    private final BankInquiryService bankInquiryService;

    public Bank(BankListRequest bankListRequest, BankInquiryService bankInquiryService) {
        this.bankListRequest = bankListRequest;
        this.bankInquiryService = bankInquiryService;
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<BankListsDto> getListBank(@PathVariable("id") String id) {
        return ResponseEntity.ok(new BankListsDto(bankListRequest.bankListRequest(id)
                .getSendMoney()
                .getResults()));
    }

    @GetMapping("/inquiry/{id}")
    public ResponseEntity<InquiryResponse> inquiryBankController(
            @PathVariable String id,
            @RequestParam String bank,
            @RequestParam("no") String inquiryNumber) {

        if (bank == null || inquiryNumber == null) {
            throw new RequestParamNotFullFilled("Bank and No must be filled");
        }

        return Optional.ofNullable(bankInquiryService.GetInquiryName(id, bank, inquiryNumber))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
