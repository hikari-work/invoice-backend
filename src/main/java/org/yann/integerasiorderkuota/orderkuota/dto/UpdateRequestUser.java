package org.yann.integerasiorderkuota.orderkuota.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRequestUser {

    @NotBlank
    private String username;

    @Email(message = "Invalid email")
    private String email;

    @JsonProperty("callback_url")
    private String callbackUrl;

    @JsonProperty("qris_string")
    private String qrisString;
    @JsonProperty("qris_image")
    private byte[] qrisImage;
    @JsonIgnore
    private String token;
}
