package nl._42.qualityws.refactoring;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl._42.qualityws.refactoring.domain.Account;
import nl._42.qualityws.refactoring.domain.AccountType;
import nl._42.qualityws.refactoring.domain.Transaction;
import nl._42.qualityws.refactoring.domain.TransactionRequest;
import nl._42.qualityws.refactoring.util.ElevenCheck;

/**
 * Contains services concerning {@link Account}.
 */
@Service
public class AccountService {

    public static final BigDecimal OWN_ACCOUNT_TRANSACTION_LIMIT = new BigDecimal("20000.00");
    public static final BigDecimal PREMIUM_TRANSACTION_LIMIT = new BigDecimal("10000.00");
    public static final BigDecimal NORMAL_TRANSACTION_LIMIT = new BigDecimal("5000.00");
    public static final BigDecimal PREMIUM_DEBT_LIMIT = new BigDecimal("-10000.00");
    public static final BigDecimal NORMAL_DEBT_LIMIT = new BigDecimal("-2000.00");

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Checks if given {@link TransactionRequest} is valid and transfers the amount of money from one {@link Account} to another.
     * 
     * @param transactionRequest TransactionRequest - request with amount, from number and to number.
     * @return Transaction - the data of the successfully executed transaction
     */
    public Transaction transfer(TransactionRequest transactionRequest) {

        // Verify all the incoming arguments are valid
        if (transactionRequest == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        if (transactionRequest.getFrom() == null || !ElevenCheck.execute(transactionRequest.getFrom().raw())) {
            throw new IllegalAccountNumberException("from");
        }
        if (transactionRequest.getTo() == null || !ElevenCheck.execute(transactionRequest.getTo().raw())) {
            throw new IllegalAccountNumberException("to");
        }
        if (transactionRequest.getAmount() == null || transactionRequest.getAmount().scale() != 2) { // ascertain we have two digits
            throw new IllegalAmountException(transactionRequest.getAmount());
        }
        if (transactionRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalAmountException(transactionRequest.getAmount());
        }

        // Verify the transfer accounts are not the same
        if (ElevenCheck.scrub(transactionRequest.getFrom().raw()).equals(ElevenCheck.scrub(transactionRequest.getTo().raw()))) {
            throw new SameAccountTransferException(transactionRequest.getFrom());
        }

        // Retrieving the from/to accounts, verify if they exist
        Account fromAccount = accountRepository.findByNumber(transactionRequest.getFrom());
        if (fromAccount == null) {
            throw new AccountDoesNotExistException(transactionRequest.getFrom());
        }
        Account toAccount = accountRepository.findByNumber(transactionRequest.getTo());
        if (toAccount == null) {
            throw new AccountDoesNotExistException(transactionRequest.getTo());
        }

        // Verify if the transaction limit is not exceeded by the transfer amount
        final BigDecimal useTransactionLimit;
        if (fromAccount.getHolder().getName().equals(toAccount.getHolder().getName())) {
            useTransactionLimit = OWN_ACCOUNT_TRANSACTION_LIMIT;
        } else if (fromAccount.getType() == AccountType.PREMIUM) {
            useTransactionLimit = PREMIUM_TRANSACTION_LIMIT;
        } else {
            useTransactionLimit = NORMAL_TRANSACTION_LIMIT;
        }
        if (useTransactionLimit.compareTo(transactionRequest.getAmount()) < 0) {
            throw new AmountExceedsTransactionLimitException(useTransactionLimit, transactionRequest.getAmount());
        }

        // Verify if the debt limit is not exceed for the from account
        final BigDecimal debtLimit;
        if (fromAccount.getType() == AccountType.PREMIUM) {
            debtLimit = PREMIUM_DEBT_LIMIT;
        } else {
            debtLimit = NORMAL_DEBT_LIMIT;
        }
        BigDecimal resultFromBalance = fromAccount.getBalance().subtract(transactionRequest.getAmount());
        if (resultFromBalance.compareTo(debtLimit) < 0) {
            throw new AmountExceedsDebtLimitException(debtLimit, resultFromBalance);
        }

        // The actual transaction; from > to
        BigDecimal resultToBalance = toAccount.getBalance().add(transactionRequest.getAmount());
        fromAccount.setBalance(resultFromBalance);
        toAccount.setBalance(resultToBalance);
        return new Transaction(fromAccount, toAccount, transactionRequest.getAmount());
    }

}
