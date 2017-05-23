package nl._42.qualityws.refactoring.domain;

/**
 * Domain class that represents the owner of a bank {@link Account}.
 */
public class AccountHolder {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
