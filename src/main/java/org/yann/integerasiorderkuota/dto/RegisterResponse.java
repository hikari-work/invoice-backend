package org.yann.integerasiorderkuota.dto;

import lombok.Data;
import org.yann.integerasiorderkuota.entity.User;

@Data
public class RegisterResponse {
    public String token;
    public String username;
    public String email;

    public RegisterResponse(User user) {
        this.token = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

}
