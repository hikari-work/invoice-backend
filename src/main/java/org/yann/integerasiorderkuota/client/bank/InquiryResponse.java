package org.yann.integerasiorderkuota.client.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InquiryResponse {

    @JsonProperty("success")
    private boolean success;
    @JsonProperty("send_money")
    private SendMoneyInquiryResult sendMoneyInquiryResult;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SendMoneyInquiryResult {
        @JsonProperty("success")
        private boolean success;
        @JsonProperty("results")
        private ResultInquiry results;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ResultInquiry {
            @JsonProperty("account_name")
            private String accountName;
        }
    }
}