package org.yann.integerasiorderkuota.orderkuota.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateOtpRequest {

    @Size(min = 1, max = 255, message = "Username must be non null")
    private String username;
    @Size(min = 1, max = 255, message = "OTP must be non null")
    private String otp;
}
