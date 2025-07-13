package org.yann.integerasiorderkuota.client.settlement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettlementDTO {
	private boolean success;

	@JsonProperty("qris_history")
	private QrisHistory qrisHistory;

	private Account account;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class QrisHistory {
		private boolean success;
		private int total;
		private int page;
		private int pages;
		private List<QrisTransaction> results;

	}
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class QrisTransaction {
		private long id;
		private String debet;
		private String kredit;

		@JsonProperty("saldo_akhir")
		private String saldoAkhir;

		private String keterangan;
		private String tanggal;
		private String status;
		private String fee;
		private Brand brand;
	}
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Brand {
		private String name;
		private String logo;
	}
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Account {
		private boolean success;
		private AccountResults results;
	}
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AccountResults {
		private long id;
		private String username;
		private String name;
		private String email;
		private String phone;
		private int balance;

		@JsonProperty("balance_str")
		private String balanceStr;

		@JsonProperty("qris_balance")
		private int qrisBalance;

		@JsonProperty("qris_balance_str")
		private String qrisBalanceStr;

		private String qrcode;
		private String qris;

		@JsonProperty("qris_name")
		private String qrisName;
	}
}
