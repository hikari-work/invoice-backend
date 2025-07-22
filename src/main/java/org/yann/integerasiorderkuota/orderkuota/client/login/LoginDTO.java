package org.yann.integerasiorderkuota.orderkuota.client.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

	private boolean success;
	private LoginResult results;
	private String message;

}
@Data
@AllArgsConstructor
@NoArgsConstructor
class LoginResult {
	private String otp;
	private String id;
	private String name;
	private String username;
	private String balance;
	private String token;
}