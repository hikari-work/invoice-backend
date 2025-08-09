package org.yann.integerasiorderkuota.orderkuota.client.settlement;

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
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
		ObjectMapper mapper = new ObjectMapper();
		SettlementDTO dto;
		try {
			dto = mapper.readValue(data.getBody(), SettlementDTO.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return dto;
	}


	public SettlementDTO getAllSettlement(String uuid, String pages) {
		User username = userService.getUserDetailsByUUID(uuid);
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("requests[qris_history][keterangan]", "");
		formData.add("requests[qris_history][jumlah]", "");
		formData.add("phone_android_version", "13");
		formData.add("app_version_code", "250711");
		formData.add("auth_username", username.getUsername());
		formData.add("requests[qris_history][page]", pages);
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

	public SettlementDTO getSettlementAllHistory(String uuid) {

		SettlementDTO dto = getAllSettlement(uuid, String.valueOf(1));
        List<SettlementDTO.QrisTransaction> list = new ArrayList<>(dto.getQrisHistory().getResults());

		int totalPages = dto.getQrisHistory().getPages();

		ExecutorService executor = Executors.newFixedThreadPool(10);

		List<CompletableFuture<List<SettlementDTO.QrisTransaction>>> futures = new ArrayList<>();

		for (int i = 2; i <= totalPages; i++) {
			final int page = i;
			CompletableFuture<List<SettlementDTO.QrisTransaction>> future = CompletableFuture.supplyAsync(() ->
					getAllSettlement(uuid, String.valueOf(page))
							.getQrisHistory()
							.getResults(), executor);
			futures.add(future);
		}

		for (CompletableFuture<List<SettlementDTO.QrisTransaction>> future : futures) {
			try {
				List<SettlementDTO.QrisTransaction> result = future.get();
				list.addAll(result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		executor.shutdown();

		list = list.stream()
				.sorted(Comparator.comparingLong(SettlementDTO.QrisTransaction::getId).reversed())
				.collect(Collectors.toList());

		dto.getQrisHistory().setResults(list);
		return dto;
	}
}
