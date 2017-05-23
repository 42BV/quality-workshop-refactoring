package nl._42.qualityws.refactoring;

import nl._42.qualityws.refactoring.domain.AccountNumber;

public class IllegalAccountNumberException extends RuntimeException {

    public IllegalAccountNumberException(String direction, AccountNumber accountNumber) {
        super("Illegal " + direction + " account " + (accountNumber == null ? null : accountNumber.raw()));
    }

}
