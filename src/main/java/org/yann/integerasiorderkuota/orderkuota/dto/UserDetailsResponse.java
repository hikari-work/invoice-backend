package org.yann.integerasiorderkuota.orderkuota.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.yann.integerasiorderkuota.orderkuota.entity.User;

@Data
public class UserDetailsResponse {

    private String username;
    private String email;
    @JsonProperty("callback_url")
    private String callbackUrl;
    private String token;
    @JsonProperty("qris_string")
    private String qrsString;

    private String qrcode;

    public UserDetailsResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.callbackUrl = user.getCallbackUrl();
        this.token = user.getId();
        this.qrsString = user.getQrisString();

        if (user.getQrisImage() != null) {
            this.qrcode = "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(user.getQrisImage());
        } else {
            this.qrcode = null;
        }
    }
}
