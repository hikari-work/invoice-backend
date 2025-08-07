package org.yann.integerasiorderkuota.orderkuota.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yann.integerasiorderkuota.orderkuota.entity.Statement;
import org.yann.integerasiorderkuota.orderkuota.entity.StatementStatus;
import org.yann.integerasiorderkuota.orderkuota.repository.StatementRepository;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;

    public boolean isExistStatementById(Long id) {
        return statementRepository.existsById(id);
    }
    public void updateStatementStatusToClaimed(Long id) {
        statementRepository.findById(id).ifPresent(statement -> {
            statement.setStatementStatus(StatementStatus.CLAIMED);
        });
    }
    public void saveStatement(Statement statement) {
        if (isExistStatementById(statement.getId())) {
            updateStatementStatusToClaimed(statement.getId());
        } else {
            statement.setStatementStatus(StatementStatus.NOT_CLAIMED);
            statementRepository.save(statement);
        }
    }

}
