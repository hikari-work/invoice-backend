package org.yann.integerasiorderkuota.bot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yann.integerasiorderkuota.bot.entity.*;
import org.yann.integerasiorderkuota.bot.repository.CustomerRepository;
import org.yann.integerasiorderkuota.bot.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomerHelper {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public boolean isCustomerHasActiveTransaction(String customer) {
        Optional<Customer> byId = customerRepository.findById(customer);
        if (byId.isEmpty()) {
            return true;
        }
        List<Transaction> byCustomer = transactionRepository.findByCustomerAndStatus(
                byId.get(), TransactionStatus.PENDING
        );
        return byCustomer.isEmpty();
    }
    public CustomerState findCustomerState(String customer) {
        return customerRepository.findById(customer)
                .map(Customer::getStatus)
                .orElse(CustomerState.IDLE);
    }
    public CustomerLevel findCustomerLevel(String customer) {
        return customerRepository.findById(customer)
                .map(Customer::getLevel)
                .orElse(CustomerLevel.USER);
    }

}
