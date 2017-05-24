package nl._42.qualityws.refactoring.domain;

import java.math.BigDecimal;

/**
 * Domain class that represents a bank account.
 */
public class Account {

    private AccountNumber accountNumber;

    private AccountHolder holder;

    private BigDecimal balance;

    private AccountType type;

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(AccountNumber accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountHolder getHolder() {
        return holder;
    }

    public void setHolder(AccountHolder holder) {
        this.holder = holder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
}
