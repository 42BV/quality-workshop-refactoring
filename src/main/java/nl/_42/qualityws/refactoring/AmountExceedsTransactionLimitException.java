package nl._42.qualityws.refactoring;

import java.math.BigDecimal;

public class AmountExceedsTransactionLimitException extends RuntimeException {

    public AmountExceedsTransactionLimitException(BigDecimal useTransactionLimit, BigDecimal amount) {
        super("Transaction amount " + amount + " exceeds transaction limit " + useTransactionLimit);
    }

}
