package org.yann.integerasiorderkuota.orderkuota.controller.v2;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yann.integerasiorderkuota.orderkuota.dto.StatementResponse;
import org.yann.integerasiorderkuota.orderkuota.entity.Statement;
import org.yann.integerasiorderkuota.orderkuota.service.StatementService;

@RestController
@RequestMapping("/api/v2/statements")
public class StatementController {

    private final StatementService statementService;

    StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("/statements/{username}")
    public ResponseEntity<StatementResponse<Statement>> getStatements(@PathVariable("username") String username,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

        Page<Statement> statements = statementService.getStatementsByUser(username, page, size);
        StatementResponse<Statement> response = new StatementResponse<>(statements);
        if (statements.hasContent()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
