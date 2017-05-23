package nl._42.qualityws.refactoring;

import java.math.BigDecimal;

public class IllegalAmountException extends RuntimeException {

    public IllegalAmountException(BigDecimal amount) {
        super(amount.toString());
    }

}
