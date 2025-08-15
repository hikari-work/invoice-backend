package org.yann.integerasiorderkuota.orderkuota.service;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yann.integerasiorderkuota.orderkuota.entity.Statement;
import org.yann.integerasiorderkuota.orderkuota.entity.StatementStatus;
import org.yann.integerasiorderkuota.orderkuota.repository.StatementRepository;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Transactional
    public void updateBulkStatement(Collection<Long> statements) {
        statementRepository.updateStatementsToClaimed(statements);
    }

    public List<Statement> getReportStatements(String username, String startDate, String endDate) {
        LocalDateTime startTime = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay();
        LocalDateTime endTime = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")). atTime(23, 59, 59);
        return statementRepository.findByUsernameAndTransferTimeBetween(username, startTime, endTime);
    }

    public byte[] getCsvReport(List<Statement> statements) {
       try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
           OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
           CSVWriter csvWriter = new CSVWriter(writer);
           csvWriter.writeNext(new String[]{"id", "username", "debet", "kredit", "keterangan", "status", "status_transfer", "transfer_time"});
           for (Statement statement : statements) {
                String[] data = {
                          String.valueOf(statement.getId()),
                          statement.getUsername(),
                          String.valueOf(statement.getDebet()),
                          String.valueOf(statement.getKredit()),
                          statement.getKeterangan(),
                          statement.getStatementStatus().name(),
                          statement.getStatementStatus().toString(),
                          statement.getTransferTime() != null ? statement.getTransferTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : ""
                };
                csvWriter.writeNext(data);

           }
           csvWriter.flush();
           return outputStream.toByteArray();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

}
