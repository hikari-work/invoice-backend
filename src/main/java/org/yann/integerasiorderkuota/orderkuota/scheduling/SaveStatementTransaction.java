package org.yann.integerasiorderkuota.orderkuota.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementDTO;
import org.yann.integerasiorderkuota.orderkuota.client.settlement.SettlementService;
import org.yann.integerasiorderkuota.orderkuota.entity.Statement;
import org.yann.integerasiorderkuota.orderkuota.entity.User;
import org.yann.integerasiorderkuota.orderkuota.service.StatementService;
import org.yann.integerasiorderkuota.orderkuota.service.UserService;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SaveStatementTransaction {


    private final UserService userService;
    private final SettlementService settlementService;
    private final StatementService statementService;

    @Scheduled(fixedRate = 10_000)
    public void saveStatementTransaction() {
        List<User> allUser = userService.getAllUser();

        allUser.parallelStream().forEach(user -> {
            SettlementDTO dto = settlementService.getSettlementAllHistory(user.getId());

            List<SettlementDTO.QrisTransaction> results = dto.getQrisHistory().getResults();

            List<Long> ids = results.stream()
                    .map(SettlementDTO.QrisTransaction::getId)
                    .toList();
            Set<Long> existingStatementIds = statementService.getAllStatementByIds(Set.copyOf(ids));
            List<Statement> newStatement = results.stream()
                    .filter(result -> !existingStatementIds.contains(result.getId()))
                    .map(result -> new Statement(result, user.getUsername()))
                    .toList();

            statementService.saveAllStatements(Set.copyOf(newStatement));
        });

    }


}
