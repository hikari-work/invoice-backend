package org.yann.integerasiorderkuota.orderkuota.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestRegisterDTO {


    @Size(min = 1, max = 255, message = "Password Not Be Empty")
    private String password;
    @Size(min = 1, max = 255, message = "Username Not Be Empty")
    private String username;
}
