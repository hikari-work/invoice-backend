package org.yann.integerasiorderkuota.orderkuota.client.otp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

@Component
@RequiredArgsConstructor
public class GenerateOTP {

	private static final Logger log = LoggerFactory.getLogger(GenerateOTP.class);

	private final RestTemplate restTemplateBuilder = new RestTemplate();
	private final UserService userService;
	String url = "https://app.orderkuota.com/api/v2/login";


	public String generateOTP(String username, String password) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("password", password);
		map.add("app_reg_id", "");
		map.add("phone_android_version", "13");
		map.add("phone_model", "SM-G981N");
		map.add("phone_uuid", "");
		map.add("app_version_code", "250711");
		map.add("ui_mode", "dark");
		map.add("username", username);

		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "okhttp/4.12.0");
		headers.set("Cookie", "");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = restTemplateBuilder.exchange(
				url,
				HttpMethod.POST,
				request,
				String.class
		);
		log.info("Request: {}", response.getStatusCode());
		log.info("Response: {}", response.getBody());
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			GenerateOtpDTO generateOtpDTO = objectMapper.readValue(response.getBody(), GenerateOtpDTO.class);
			if (!generateOtpDTO.isSuccess()) {
				return generateOtpDTO.getMessage();
			}

			userService.saveEmail(generateOtpDTO.getResults().getOtp(), username);
			return generateOtpDTO.getResults().getOtpValue();
		} catch (Exception e) {
			log.error("Error parsing response", e);
			return null;
		}
	}
}
