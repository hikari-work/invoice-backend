package org.yann.integerasiorderkuota.client.otp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateOtpDTO {
	private boolean success;
	private Results results;
	private String message;


}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Results {
	public String otp;
	@JsonProperty("otp_value")
	public String otpValue;
}
