package org.yann.integerasiorderkuota.orderkuota.client.bank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

import java.util.Map;

@Slf4j
@Component
public class BankListRequest {


    private final RestTemplate restTemplateBuilder = new RestTemplate();
    private final UserService userService;

    public BankListRequest(UserService userService) {
        this.userService = userService;
    }

    public BankResponse bankListRequest(String userId) {
        User user = userService.getUserDetailsByUsername(userId);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("request_time", String.valueOf(System.currentTimeMillis()));
        form.add("app_reg_id", "cW7nrZuwSTmHMUq38nsYDt:APA91bHtuZh8Rtb4tu4-QkspgkW8WbHF2ZLMHqNesxgpdIDj502JW927xzpwbKPeBt11SYvkFHtHKCVM3rhUkjCku4g5Bm0CD76ACG5ABGy-JfVXdMi09sU");
        form.add("phone_uuid", "cW7nrZuwSTmHMUq38nsYDt");
        form.add("auth_username", user.getUsername());
        form.add("auth_token", user.getToken());
        form.add("app_version_name", "25.07.11");
        form.add("app_version_code", "250711");
        form.add("ui_mode", "dark");
        form.add("requests[send_money][action]", "get");
        form.add("requests[0]", "account");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "okhttp/4.12.0");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
        String url = "https://app.orderkuota.com/api/v2/get";
        ResponseEntity<BankResponse> data = restTemplateBuilder.exchange(
                url,
                HttpMethod.POST,
                request,
                BankResponse.class
        );
        log.info("Data {}", data.getBody());
        Map<String, Object> banksmap = null;
        if (data.getBody() != null) {
            banksmap = data.getBody().getSendMoney().getResults().getBanks();
        }
        if (banksmap != null) {
            banksmap.values().removeIf(bank -> bank.equals("0") || bank.equals("1") || bank.equals("2") || bank.equals("3"));
        }
        data.getBody().getSendMoney().getResults().setBanks(banksmap);
        return data.getBody();
    }
}
