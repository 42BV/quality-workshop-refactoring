package nl._42.qualityws.refactoring.domain;

/**
 * Domain class that represents a id number of a bank {@link Account}.
 */
public class AccountNumber {

    private final String rawAccountNumber;

    public AccountNumber(String rawAccountNumber) {
        this.rawAccountNumber = rawAccountNumber;
    }

    public String raw() {
        return rawAccountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountNumber that = (AccountNumber) o;
        return rawAccountNumber != null ? rawAccountNumber.equals(that.rawAccountNumber) : that.rawAccountNumber == null;
    }

    @Override
    public int hashCode() {
        return rawAccountNumber != null ? rawAccountNumber.hashCode() : 0;
    }

}
