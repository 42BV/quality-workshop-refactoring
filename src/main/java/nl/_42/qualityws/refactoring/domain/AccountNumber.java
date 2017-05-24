package nl._42.qualityws.refactoring.domain;

import nl._42.qualityws.refactoring.util.ElevenCheck;

/**
 * Domain class that represents a id number of a bank {@link Account}.
 */
public class AccountNumber {

    private final String accountNumber;

    public AccountNumber(String rawAccountNumber) {
        if (!ElevenCheck.execute(rawAccountNumber)) {
            throw new IllegalAccountNumberException(rawAccountNumber);
        }
        this.accountNumber = ElevenCheck.scrub(rawAccountNumber);
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountNumber that = (AccountNumber) o;
        return accountNumber != null ? accountNumber.equals(that.accountNumber) : that.accountNumber == null;
    }

    @Override
    public int hashCode() {
        return accountNumber != null ? accountNumber.hashCode() : 0;
    }

}
