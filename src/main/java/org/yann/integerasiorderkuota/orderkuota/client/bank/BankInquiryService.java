package org.yann.integerasiorderkuota.orderkuota.client.bank;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementDTO;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementService;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.exception.BankNotFound;
import org.yann.integerasiorderkuota.orderkuota.exception.InquiryNotFound;
import org.yann.integerasiorderkuota.orderkuota.exception.UserBalanceUnderMinimum;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

@Component
public class BankInquiryService {

    private static final Logger log = LoggerFactory.getLogger(BankInquiryService.class);
    private final SettlementService settlementService;
    private final UserService userService;
    private final BankListRequest bankListRequest;
    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BankInquiryService(SettlementService settlementService, UserService userService, BankListRequest bankListRequest) {
        this.settlementService = settlementService;
        this.userService = userService;
        this.bankListRequest = bankListRequest;
    }

    public boolean userBalance(String userId) {
        User user = userService.getUserDetailsByUsername(userId);
        SettlementDTO settlement = settlementService.getSettlement(user.getId());
        if (settlement.getAccount().getResults().getBalance() <= 12000) {
            throw new UserBalanceUnderMinimum("Saldo minimal harus Rp12.000");
        }
        return true;
    }
    public boolean isValidBankName(String bankName, String userId) {
        BankResponse response = bankListRequest.bankListRequest(userId);
        if (response.getSendMoney().getResults().getBanks().get(bankName) == null) {
            throw new BankNotFound("Bank " + bankName + " Tidak Ditemukan");
        }
        return true;
    }
    public InquiryResponse GetInquiryName(@Nonnull String userId,@Nonnull String bankName, @Nonnull String accountNumber) {
        if (!userBalance(userId) || !isValidBankName(bankName, userId)) {
            throw new RuntimeException("Terjadi kesalahan");
        }
        User user = userService.getUserDetailsByUsername(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("signature", "a0c565d51ddfa36cc8a03d15110067d4f4b88e407963d222d87eab158acc0d7bc212e720f9097dc8014eec2b185a9b5eee12751e174ab06f4d9aff4cbf498f9c");
        headers.set("timestamp", String.valueOf(System.currentTimeMillis()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept-Encoding", "application/json");
        headers.set("User-Agent", "okhttp/4.12.0");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("requests[send_money][amount]", "10000");
        body.add("app_reg_id", "cW7nrZuwSTmHMUq38nsYDt:APA91bHtuZh8Rtb4tu4-QkspgkW8WbHF2ZLMHqNesxgpdIDj502JW927xzpwbKPeBt11SYvkFHtHKCVM3rhUkjCku4g5Bm0CD76ACG5ABGy-JfVXdMi09sU");
        body.add("phone_uuid", "cW7nrZuwSTmHMUq38nsYDt");
        body.add("requests[send_money][account_number]", accountNumber);
        body.add("phone_model", "SM-G981N");
        body.add("requests[send_money][bank]", bankName);
        body.add("request_time", String.valueOf(System.currentTimeMillis()));
        body.add("phone_android_version", "13");
        body.add("app_version_code", "250811");
        body.add("auth_username", userId);
        body.add("requests[send_money][branch]", "");
        body.add("auth_token", user.getToken());
        body.add("app_version_name", "25.08.11");
        body.add("requests[send_money][action]", "check");
        body.add("ui_mode", "dark");


        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            String url = "https://app.orderkuota.com/api/v2/get";
            ResponseEntity<String> response = restTemplateBuilder.build()
                    .postForEntity(
                            url,
                            requestEntity,
                            String.class);
            log.info(response.toString());
            InquiryResponse body1 = objectMapper.readValue(response.getBody(), InquiryResponse.class);
            if (body1 != null && body1.getSendMoneyInquiryResult().getResults().getAccountName().equals("INVALID_ACCOUNT_NUMBER")) {
                throw new InquiryNotFound("Not Found");
            }
            return body1;

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new InquiryNotFound("Not Found");
        }
    }

}
