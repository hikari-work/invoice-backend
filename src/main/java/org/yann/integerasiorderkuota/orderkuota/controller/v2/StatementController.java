package org.yann.integerasiorderkuota.orderkuota.controller.v2;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.yann.integerasiorderkuota.orderkuota.dto.StatementResponse;
import org.yann.integerasiorderkuota.orderkuota.entity.Statement;
import org.yann.integerasiorderkuota.orderkuota.service.StatementService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v2/statements")
public class StatementController {

    private final StatementService statementService;

    StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("/{username}")
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
    @GetMapping("/report/{username}")
    @Async("asyncExecutor")
    public CompletableFuture<ResponseEntity<?>> getReport(@PathVariable("username") String username,
                                                                   @RequestParam(value = "start_date") String startDate,
                                                                   @RequestParam(value = "end_date") String endDate,
                                                               @RequestParam(value = "type", defaultValue = "json") String type) {

        return CompletableFuture.supplyAsync(()-> statementService.getReportStatements(username, startDate, endDate))
                .thenApplyAsync(data -> {
                    return switch (type) {
                        case "json" -> ResponseEntity.ok(data);
                        case "csv" -> ResponseEntity.ok(statementService.getCsvReport(data));
                        default -> ResponseEntity.badRequest().body("Unsupported report type: " + type);
                    };
        });
    }

}
