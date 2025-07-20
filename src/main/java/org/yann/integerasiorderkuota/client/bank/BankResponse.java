package org.yann.integerasiorderkuota.client.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("send_money")
    private SendMoneyDto sendMoney;

    @JsonProperty("account")
    private AccountDto account;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SendMoneyDto {
        @JsonProperty("success")
        private boolean success;
        @JsonProperty("message")
        private String message;
        @JsonProperty("results")
        private SendMoneyResultsDto results;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SendMoneyResultsDto {
        @JsonProperty("min")
        private long min;
        @JsonProperty("max")
        private long max;
        @JsonProperty("fee")
        private int fee;
        @JsonProperty("banks")
        private Map<String, Object> banks;
        @JsonProperty("amount_limit")
        private String amountLimit;
        @JsonProperty("account_limit")
        private String accountLimit;
        @JsonProperty("operational_time")
        private Map<String, String> operationalTime;
        @JsonProperty("branches")
        private Map<String, String> branches;
        @JsonProperty("info")
        private InfoContainerDto info;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BankDetailDto {
        @JsonProperty("name")
        private String name;
        @JsonProperty("fee")
        private int fee;
        @JsonProperty("status")
        private String status;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoContainerDto {
        @JsonProperty("tos")
        private TosDto tos;
        @JsonProperty("powered")
        private PoweredByDto powered;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TosDto {
        @JsonProperty("title")
        private String title;
        @JsonProperty("contents")
        private List<String> contents;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PoweredByDto {
        @JsonProperty("title")
        private String title;
        @JsonProperty("image")
        private String image;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountDto {
        @JsonProperty("success")
        private boolean success;
        @JsonProperty("results")
        private AccountResultsDto results;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountResultsDto {
        @JsonProperty("id")
        private long id;
        @JsonProperty("username")
        private String username;
        @JsonProperty("name")
        private String name;
        @JsonProperty("email")
        private String email;
        @JsonProperty("phone")
        private String phone;
        @JsonProperty("balance")
        private long balance;
        @JsonProperty("balance_str")
        private String balanceStr;
        @JsonProperty("qris_balance")
        private long qrisBalance;
        @JsonProperty("qris_balance_str")
        private String qrisBalanceStr;
        @JsonProperty("qrcode")
        private String qrcode;
        @JsonProperty("qris")
        private String qris;
        @JsonProperty("qris_name")
        private String qrisName;
    }

}