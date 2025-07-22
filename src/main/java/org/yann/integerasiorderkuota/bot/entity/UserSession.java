package org.yann.integerasiorderkuota.bot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    public String userId;
    public CustomerState customerState;

    public String messageId;

    public Draft draft;
}
