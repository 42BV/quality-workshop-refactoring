package nl._42.qualityws.refactoring.domain;

import java.math.BigDecimal;

/**
 * Domain class that represents a request for the transfer of an amount of money from one {@link Account} with {@link AccountNumber} to another.
 */
public class TransactionRequest {

    private AccountNumber from;

    private AccountNumber to;

    private BigDecimal amount;

    public TransactionRequest(AccountNumber from, AccountNumber to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public AccountNumber getFrom() {
        return from;
    }

    public AccountNumber getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
