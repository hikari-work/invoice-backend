package org.yann.integerasiorderkuota.orderkuota.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementDTO;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementService;
import org.yann.integerasiorderkuota.orderkuota.entity.User;

@Service
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
	private final SettlementService settlementService;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper;

	public TransactionService(SettlementService settlementService, ObjectMapper objectMapper) {
		this.settlementService = settlementService;
		this.objectMapper = objectMapper;
	}

	public SettlementDTO sendCallback(User user) {
		getMenu(user.getUsername(), user.getToken());
		getQrisMenu(user.getUsername(), user.getToken());
		SettlementDTO settlement = settlementService.getSettlement(user.getId());
		if (settlement == null) {
			log.error("Failed to get settlement data for user: {}", user.getUsername());
			return null;
		}
		
		String callbackUrl = user.getCallbackUrl();
		if (callbackUrl == null || callbackUrl.isEmpty()) {
			log.error("Callback URL is null or empty for user: {}", user.getUsername());
			return settlement;
		}
		
		// Validate the callback URL
		if (!callbackUrl.startsWith("http://") && !callbackUrl.startsWith("https://")) {
			log.error("Callback URL is not absolute: {}", callbackUrl);
			return settlement;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SettlementDTO> request = new HttpEntity<>(settlement, headers);
		
		try {
			log.info("Sending callback to: {}", callbackUrl);
			ResponseEntity<String> response = restTemplate.exchange(
					callbackUrl,
					HttpMethod.POST,
					request,
					String.class
			);
			log.info("Callback Response Status: {}", response.getStatusCode());
			
			if (response.getBody() != null) {
				try {
					SettlementDTO settlementDTO = objectMapper.readValue(response.getBody(), SettlementDTO.class);
					log.info("Callback Response processed successfully");
					return settlementDTO;
				} catch (Exception e) {
					log.error("Failed to parse callback response: {}", e.getMessage());
					return settlement;
				}
			} else {
				log.warn("Callback response body is null");
				return settlement;
			}
		} catch (Exception e) {
			log.error("Callback Failed: {}", e.getMessage());
			return settlement;
		}
	}
	public void getMenu(String username, String token) {
		log.info("Started get Menu....");
		String url = "https://app.orderkuota.com/api/v2/get";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("accept-encoding", "gzip");
		headers.set("user-agent", "okhttp/4.12.0");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("ui_mode", "dark");
		body.add("requests[9]", "top_menu_v2");
		body.add("requests[8]", "config");
		body.add("requests[6]", "unread_notification_count");
		body.add("requests[5]", "bottom_menu");
		body.add("requests[4]", "point");
		body.add("requests[3]", "total_pending_trx");
		body.add("requests[2]", "payments");
		body.add("requests[1]", "navigation_menu");
		body.add("requests[0]", "account");
		body.add("phone_uuid", "cW7nrZuwSTmHMUq38nsYDt");
		body.add("phone_model", "SM-G981N");
		body.add("phone_android_version", "13");
		body.add("auth_username", username);
		body.add("auth_token", token);
		body.add("app_version_name", "25.07.11");
		body.add("app_version_code", "250711");
		body.add("app_reg_id", "cW7nrZuwSTmHMUq38nsYDt:APA91bHtuZh8Rtb4tu4-QkspgkW8WbHF2ZLMHqNesxgpdIDj502JW927xzpwbKPeBt11SYvkFHtHKCVM3rhUkjCku4g5Bm0CD76ACG5ABGy-JfVXdMi09sU");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

		restTemplate.postForEntity(url, request, String.class);
		log.info("Finished get Menu....");
	}
	public void getQrisMenu(String username, String token) {
		log.info("Started get Qris Menu....");
		String url = "https://app.orderkuota.com/api/v2/get";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("accept-encoding", "gzip");
		headers.set("user-agent", "okhttp/4.12.0");
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("requests[splash_screen][screen_width]", "1080");
		body.add("requests[9]", "banner");
		body.add("app_reg_id", "cW7nrZuwSTmHMUq38nsYDt:APA91bHtuZh8Rtb4tu4-QkspgkW8WbHF2ZLMHqNesxgpdIDj502JW927xzpwbKPeBt11SYvkFHtHKCVM3rhUkjCku4g5Bm0CD76ACG5ABGy-JfVXdMi09sU");
		body.add("requests[6]", "config");
		body.add("phone_uuid", "cW7nrZuwSTmHMUq38nsYDt");
		body.add("requests[5]", "show_hide_image");
		body.add("requests[8]", "top_menu_v2");
		body.add("requests[7]", "bottom_menu");
		body.add("phone_model", "SM-G981N");
		body.add("phone_android_version", "13");
		body.add("app_version_code", "250711");
		body.add("auth_username", username);
		body.add("requests[1]", "main_page");
		body.add("requests[4]", "payments");
		body.add("requests[3]", "product_layout");
		body.add("auth_token", token);
		body.add("app_version_name", "25.07.11");
		body.add("requests[10]", "app_config");
		body.add("ui_mode", "dark");
		body.add("requests[0]", "products");
		body.add("requests[splash_screen][screen_height]", "2307");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		restTemplate.postForEntity(url, request, String.class);
		log.info("Finished get Qris Menu....");
	}

}