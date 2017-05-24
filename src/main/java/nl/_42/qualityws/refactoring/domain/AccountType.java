package nl._42.qualityws.refactoring.domain;

import java.math.BigDecimal;

/**
 * Type of {@link Account}.
 * - The holder of a PREMIUM account has privileges on debt limit over the holder of a NORMAL account.
 */
public enum AccountType {
    PREMIUM {
        @Override
        public BigDecimal getDebtLimit() {
            return PREMIUM_DEBT_LIMIT;
        }

        @Override
        public BigDecimal getTransactionLimit() {
            return PREMIUM_TRANSACTION_LIMIT;
        }
    },
    NORMAL {
        @Override
        public BigDecimal getDebtLimit() {
            return NORMAL_DEBT_LIMIT;
        }

        @Override
        public BigDecimal getTransactionLimit() {
            return NORMAL_TRANSACTION_LIMIT;
        }
    };    
    
    public static final BigDecimal PREMIUM_DEBT_LIMIT = new BigDecimal("-10000.00");
    public static final BigDecimal NORMAL_DEBT_LIMIT = new BigDecimal("-2000.00");
    
    public static final BigDecimal PREMIUM_TRANSACTION_LIMIT = new BigDecimal("10000.00");
    public static final BigDecimal NORMAL_TRANSACTION_LIMIT = new BigDecimal("5000.00");
    
    public abstract BigDecimal getDebtLimit();
    public abstract BigDecimal getTransactionLimit();
}
