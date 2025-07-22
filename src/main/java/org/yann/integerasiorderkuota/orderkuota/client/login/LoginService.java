package org.yann.integerasiorderkuota.orderkuota.client.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;


@Component
@RequiredArgsConstructor
public class LoginService {

	private static final Logger log = LoggerFactory.getLogger(LoginService.class);
	private final RestTemplate restTemplateBuilder = new RestTemplate();
	private final UserService userService;

	public String loginOrkut(String username, String loginOtp) {
		String url = "https://app.orderkuota.com/api/v2/login";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("password", loginOtp);
		map.add("app_reg_id", "");
		map.add("phone_android_version", "13");
		map.add("phone_model", "SM-G981N");
		map.add("phone_uuid", "");
		map.add("app_version_code", "250711");
		map.add("ui_mode", "dark");
		map.add("username", username);

		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "okhttp/4.12.0");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<String> response = restTemplateBuilder.exchange(
				url,
				HttpMethod.POST,
				request,
				String.class
		);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			LoginDTO loginDTO = objectMapper.readValue(response.getBody(), LoginDTO.class);
			if (!loginDTO.isSuccess()) {
				return null;
			}
			userService.saveToken(loginDTO.getResults().getToken(), username);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		log.info("Response : {}", response.getBody());
		return response.getBody();
	}

}
