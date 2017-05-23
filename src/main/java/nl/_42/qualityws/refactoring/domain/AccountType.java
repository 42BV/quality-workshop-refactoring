package nl._42.qualityws.refactoring.domain;

/**
 * Type of {@link Account}.
 * - The holder of a PREMIUM account has privileges on debt limit over the holder of a NORMAL account.
 */
public enum AccountType {
    PREMIUM, NORMAL
}
