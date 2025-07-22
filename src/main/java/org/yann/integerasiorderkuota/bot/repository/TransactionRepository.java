package org.yann.integerasiorderkuota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yann.integerasiorderkuota.bot.entity.Customer;
import org.yann.integerasiorderkuota.bot.entity.Transaction;
import org.yann.integerasiorderkuota.bot.entity.TransactionStatus;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByCustomer(Customer customer);

    List<Transaction> findByCustomerAndStatus(Customer customer, TransactionStatus status);
}
