package nl._42.qualityws.refactoring;

import nl._42.qualityws.refactoring.domain.AccountNumber;

public class AccountDoesNotExistException extends RuntimeException {

    public AccountDoesNotExistException(AccountNumber accountNumber) {
        super(accountNumber.raw());
    }

}
