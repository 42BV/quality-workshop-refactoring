package nl._42.qualityws.refactoring;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl._42.qualityws.refactoring.domain.Account;
import nl._42.qualityws.refactoring.domain.Transaction;
import nl._42.qualityws.refactoring.domain.TransactionRequest;

/**
 * Contains services concerning {@link Account}.
 */
@Service
public class AccountService {

    public static final BigDecimal OWN_ACCOUNT_TRANSACTION_LIMIT = new BigDecimal("20000.00");

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Checks if given {@link TransactionRequest} is valid and transfers the amount of money from one {@link Account} to another.
     * 
     * @param transactionRequest TransactionRequest - request with amount, from number and to number.
     * @return Transaction - the data of the successfully executed transaction
     */
    public Transaction transfer(TransactionRequest transactionRequest) {

        if (transactionRequest == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }

        BigDecimal amount = transactionRequest.getAmount();
        Account fromAccount = accountRepository.findByNumber(transactionRequest.getFrom());
        Account toAccount = accountRepository.findByNumber(transactionRequest.getTo());

        verifyTransactionLimit(amount, fromAccount, toAccount);
        verifyDebtLimit(amount, fromAccount);

        return executeTransaction(amount, fromAccount, toAccount);
    }

    private void verifyTransactionLimit(BigDecimal amount, Account fromAccount, Account toAccount) {
        final BigDecimal useTransactionLimit;
        if (fromAccount.getHolder().equals(toAccount.getHolder())) {
            useTransactionLimit = OWN_ACCOUNT_TRANSACTION_LIMIT;
        } else {
            useTransactionLimit = fromAccount.getType().getTransactionLimit();
        }
        if (useTransactionLimit.compareTo(amount) < 0) {
            throw new AmountExceedsTransactionLimitException(useTransactionLimit, amount);
        }
    }
    
    private BigDecimal verifyDebtLimit(BigDecimal amount, Account fromAccount) {
        final BigDecimal debtLimit = fromAccount.getType().getDebtLimit();
        BigDecimal resultFromBalance = fromAccount.getBalance().subtract(amount);
        if (resultFromBalance.compareTo(debtLimit) < 0) {
            throw new AmountExceedsDebtLimitException(debtLimit, resultFromBalance);
        }
        return resultFromBalance;
    }

    private Transaction executeTransaction(BigDecimal amount, Account fromAccount, Account toAccount) {
        BigDecimal resultFromBalance = fromAccount.getBalance().subtract(amount);
        BigDecimal resultToBalance = toAccount.getBalance().add(amount);
        fromAccount.setBalance(resultFromBalance);
        toAccount.setBalance(resultToBalance);
        return new Transaction(fromAccount, toAccount, amount);
    }
}
