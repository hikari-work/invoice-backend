package org.yann.integerasiorderkuota.orderkuota.exception;

public class UserBalanceUnderMinimum extends RuntimeException {
    public UserBalanceUnderMinimum(String message) {
        super(message);
    }
}
