package org.yann.integerasiorderkuota.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yann.integerasiorderkuota.client.settlement.SettlementDTO;
import org.yann.integerasiorderkuota.entity.User;
import org.yann.integerasiorderkuota.exception.UserNotFoundException;
import org.yann.integerasiorderkuota.service.TransactionService;
import org.yann.integerasiorderkuota.service.UserService;

@RestController
@RequestMapping("/api")
public class GetTransaction {

	private static final Logger log = LoggerFactory.getLogger(GetTransaction.class);
	private final TransactionService transactionService;
	private final UserService userService;

	public GetTransaction(TransactionService transactionService, UserService userService) {
		this.transactionService = transactionService;
		this.userService = userService;
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
}