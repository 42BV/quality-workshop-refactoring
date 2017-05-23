package nl._42.qualityws.refactoring;

import nl._42.qualityws.refactoring.domain.AccountNumber;

public class SameAccountTransferException extends RuntimeException {

    public SameAccountTransferException(AccountNumber account) {
        super("Same account number: " + account.raw());
    }
}
