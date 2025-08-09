package org.yann.integerasiorderkuota.orderkuota.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.yann.integerasiorderkuota.orderkuota.entity.Statement;
import org.yann.integerasiorderkuota.orderkuota.entity.StatementStatus;
import org.yann.integerasiorderkuota.orderkuota.repository.StatementRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            statementRepository.save(statement);
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
    public Set<Long> getAllStatementByIds(Set<Long> id) {
        return statementRepository.findAllById(id)
                .stream()
                .map(Statement::getId)
                .collect(Collectors.toSet());
    }
    public void saveAllStatements(Set<Statement> statements) {
        statementRepository.saveAll(statements);
    }

    public Page<Statement> getStatementsByUser(String username, int page, int size) {
        return statementRepository.findByUsername(username, PageRequest.of(page, size));
    }

    public List<Statement> getPendingStatement() {
        return statementRepository.findByStatementStatus(StatementStatus.NOT_CLAIMED);
    }

    public void updateBulkStatement(Collection<String> statements) {
        statementRepository.updateStatementsToClaimed(statements);
    }

}
