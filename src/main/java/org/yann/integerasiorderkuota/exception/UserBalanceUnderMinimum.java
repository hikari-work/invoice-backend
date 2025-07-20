package org.yann.integerasiorderkuota.exception;

public class UserBalanceUnderMinimum extends RuntimeException {
    public UserBalanceUnderMinimum(String message) {
        super(message);
    }
}
