package org.yann.integerasiorderkuota.orderkuota.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yann.integerasiorderkuota.orderkuota.client.bank.*;
import org.yann.integerasiorderkuota.orderkuota.exception.RequestParamNotFullFilled;

@RestController
@RequestMapping("/api/bank")
public class Bank {
    private final BankListRequest bankListRequest;
    private final BankInquiryService bankInquiryService;

    public Bank(BankListRequest bankListRequest, BankInquiryService bankInquiryService) {
        this.bankListRequest = bankListRequest;
        this.bankInquiryService = bankInquiryService;
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<BankListsDto> getListBank(@PathVariable("id") String id) {
        BankResponse bankResponse = bankListRequest.bankListRequest(id);
        BankListsDto bankListsDto = new BankListsDto(bankResponse.getSendMoney().getResults());
        return ResponseEntity.ok(bankListsDto);
    }

    @GetMapping("/inquiry/{id}")
    public ResponseEntity<InquiryResponse> inquiryBankController(
            @PathVariable("id") String id,
            @RequestParam(value = "bank") String bank,
            @RequestParam(value = "no") String inquiryNumber
    ) {
        if (bank == null || inquiryNumber == null) {
            throw new RequestParamNotFullFilled("Bank And No Must Be Filled");
        }
        InquiryResponse inquiryResponse = bankInquiryService.GetInquiryName(id, bank, inquiryNumber);
        return inquiryResponse != null ? ResponseEntity.ok(inquiryResponse) : ResponseEntity.notFound().build();

    }


}
