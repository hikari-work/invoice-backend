package org.yann.integerasiorderkuota.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDTO <T>{
    private String status;
    private T data;
}
