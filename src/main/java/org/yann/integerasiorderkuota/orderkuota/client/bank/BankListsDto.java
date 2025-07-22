package org.yann.integerasiorderkuota.orderkuota.client.bank;



import lombok.Data;

import java.util.Map;

@Data
public class BankListsDto {
    private Map<String, Object> banks;

    public BankListsDto(BankResponse.SendMoneyResultsDto results) {
        this.banks = results.getBanks();
    }
}