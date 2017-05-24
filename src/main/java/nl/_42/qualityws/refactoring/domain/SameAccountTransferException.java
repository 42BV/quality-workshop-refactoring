package nl._42.qualityws.refactoring.domain;

public class SameAccountTransferException extends RuntimeException {

    public SameAccountTransferException(AccountNumber account) {
        super("Same account number: " + account.getAccountNumber());
    }
}
