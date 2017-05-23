package nl._42.qualityws.refactoring.domain;

import java.math.BigDecimal;

/**
 * Domain class that represent a successful transfer of an amount of money from one {@link Account} to another.
 */
public class Transaction {

    private Account from;

    private Account to;

    private BigDecimal amount;

    public Transaction(Account from, Account to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Account getFrom() {
        return from;
    }

    public Account getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
