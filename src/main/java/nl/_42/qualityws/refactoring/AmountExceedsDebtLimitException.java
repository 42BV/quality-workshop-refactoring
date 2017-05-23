package nl._42.qualityws.refactoring;

import java.math.BigDecimal;

public class AmountExceedsDebtLimitException extends RuntimeException {

    public AmountExceedsDebtLimitException(BigDecimal debtLimit, BigDecimal amount) {
        super("Transaction amount " + amount + " exceeds debt limit " + debtLimit);
    }

}
