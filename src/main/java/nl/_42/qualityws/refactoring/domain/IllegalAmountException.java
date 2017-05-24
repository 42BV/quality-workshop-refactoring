package nl._42.qualityws.refactoring.domain;

import java.math.BigDecimal;

public class IllegalAmountException extends RuntimeException {

    public IllegalAmountException(BigDecimal amount) {
        super(amount.toString());
    }

}
