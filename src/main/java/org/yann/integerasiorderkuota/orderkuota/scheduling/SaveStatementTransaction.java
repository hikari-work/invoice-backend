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

            for (SettlementDTO.QrisTransaction result : results) {
                if (!statementService.isExistStatementById(result.getId())) {
                    Statement statement = new Statement(result, user.getUsername());
                    statementService.saveStatement(statement);
                }
            }
        });
    }


}
