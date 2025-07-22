package org.yann.integerasiorderkuota.orderkuota.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementDTO;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementService;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.exception.UserNotFoundException;
import org.yann.integerasiorderkuota.orderkuota.service.TransactionService;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

@RestController
@RequestMapping("/api")
public class GetTransaction {

	private static final Logger log = LoggerFactory.getLogger(GetTransaction.class);
	private final TransactionService transactionService;
	private final UserService userService;
	private final SettlementService settlementService;

	public GetTransaction(TransactionService transactionService, UserService userService, SettlementService settlementService) {
		this.transactionService = transactionService;
		this.userService = userService;
		this.settlementService = settlementService;
	}

	@GetMapping("/history/{id}")
	public ResponseEntity<SettlementDTO> getAllData(@PathVariable("id") String userId) {
		log.info("Username is {}", userId);
		User user = userService.getUserDetailsByUsername(userId);
		if (user == null) {
			throw new UserNotFoundException("User Not Found");
		}
		SettlementDTO settlementDTO = transactionService.sendCallback(user);
		if (settlementDTO != null) {
			return ResponseEntity.ok(settlementDTO);
		} else {
			log.error("Settlement data is null");
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/mutasi/{id}")
	public ResponseEntity<SettlementDTO> getAllSettlement(@PathVariable("id") String userId) {
		log.info("Username is {}", userId);
		User user = userService.getUserDetailsByUsername(userId);
		if (user == null) {
			throw new UserNotFoundException("User Not Found");
		}
		SettlementDTO settlementDTO = settlementService.getSettlementAllHistory(user.getId());

		if (settlementDTO != null) {
			settlementDTO.getQrisHistory().setPage(null);
			settlementDTO.getQrisHistory().setPages(null);
			return ResponseEntity.ok(settlementDTO);
		} else {
			log.error("Settlement data is null");
			return ResponseEntity.notFound().build();
		}
	}
}