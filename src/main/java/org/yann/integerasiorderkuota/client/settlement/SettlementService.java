package org.yann.integerasiorderkuota.client.settlement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.yann.integerasiorderkuota.entity.User;
import org.yann.integerasiorderkuota.service.UserService;


import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class SettlementService {


	private static final Logger log = LoggerFactory.getLogger(SettlementService.class);
	private final RestTemplate restTemplateBuilder = new RestTemplate();
	private final UserService userService;

	public SettlementDTO getSettlement(String uuid) {
		User username = userService.getUserDetailsByUUID(uuid);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("requests[qris_history][keterangan]", "");
		formData.add("requests[qris_history][jumlah]", "");
		formData.add("phone_android_version", "13");
		formData.add("app_version_code", "250711");
		formData.add("auth_username", username.getUsername());
		formData.add("requests[qris_history][page]", "1");
		formData.add("auth_token", username.getToken());
		formData.add("app_version_name", "25.07.11");
		formData.add("ui_mode", "dark");
		formData.add("requests[qris_history][dari_tanggal]", "");
		formData.add("requests[0]", "account");
		formData.add("requests[qris_history][ke_tanggal]", "");
		formData.add("app_reg_id", "cW7nrZuwSTmHMUq38nsYDt:APA91bHtuZh8Rtb4tu4-QkspgkW8WbHF2ZLMHqNesxgpdIDj502JW927xzpwbKPeBt11SYvkFHtHKCVM3rhUkjCku4g5Bm0CD76ACG5ABGy-JfVXdMi09sU");
		formData.add("phone_uuid", "cW7nrZuwSTmHMUq38nsYDt");


		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("User-Agent", "okhttp/4.12.0");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
		String url = "https://app.orderkuota.com/api/v2/get";
		ResponseEntity<String> data = restTemplateBuilder.exchange(
				url,
				HttpMethod.POST,
				request,
				String.class
		);
		log.info("Hasil : {}", data.getBody());
		ObjectMapper mapper = new ObjectMapper();
		SettlementDTO dto;
		try {
			dto = mapper.readValue(data.getBody(), SettlementDTO.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return dto;
	}
}
