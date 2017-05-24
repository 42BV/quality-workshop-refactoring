package nl._42.qualityws.refactoring.domain;

/**
 * Domain class that represents a id number of a bank {@link Account}.
 */
public class AccountNumber {

    private final String rawRekeningNummer;

    public AccountNumber(String rawRekeningNummer) {
        this.rawRekeningNummer = rawRekeningNummer;
    }

    public String raw() {
        return rawRekeningNummer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountNumber that = (AccountNumber) o;
        return rawRekeningNummer != null ? rawRekeningNummer.equals(that.rawRekeningNummer) : that.rawRekeningNummer == null;
    }

    @Override
    public int hashCode() {
        return rawRekeningNummer != null ? rawRekeningNummer.hashCode() : 0;
    }

}
