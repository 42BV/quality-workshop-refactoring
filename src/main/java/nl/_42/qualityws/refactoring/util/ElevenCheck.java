package nl._42.qualityws.refactoring.util;

/**
 * Utility class that provides a method to check if a given accountNumber succeeds the '11-check'.
 * https://nl.wikipedia.org/wiki/Elfproef.
 */
public class ElevenCheck {

    /**
     * Scrubs a given raw alphanumeric accountNumber to a only-digit one.
     * The result will also be left-padded with zero's to a length of 10 digits.
     * 
     * @param accountNumber String - the raw accountnumber
     * @return String - only-digit left-padded zero's accountnumber
     */
    public static String scrub(String accountNumber) {

        // accountnumber may not be null
        if (accountNumber == null || accountNumber.length() == 0) {
            throw new IllegalArgumentException("Account number must exist.");
        }

        // Strip all non-digits
        StringBuilder accountNumberOnlyDigits = new StringBuilder();
        for (String character : accountNumber.split("")) {
            Character currentChar = character.charAt(0);
            if (Character.isDigit(currentChar)) {
                accountNumberOnlyDigits.append(currentChar);
            }
        }

        // Check if accountNumber does not only contain zero's
        boolean onlyZeroes = true;
        for (String number : accountNumberOnlyDigits.toString().split("")) {
            if (!number.equals("0")) {
                onlyZeroes = false;
                break;
            }
        }
        if (onlyZeroes) {
            throw new IllegalArgumentException("Account number with only zero's not permitted.");
        }

        // Check max length of 10 digits
        if (accountNumberOnlyDigits.length() > 10) {
            throw new IllegalArgumentException("Account number may not contain more than 10 digits.");
        }

        // Left pad account number with zero's to 10 digits
        StringBuilder accountNumberLengthTen = new StringBuilder();
        for (int position = accountNumberOnlyDigits.length(); position < 10; position++) {
            accountNumberLengthTen.append('0');
        }
        accountNumberLengthTen.append(accountNumberOnlyDigits);

        return accountNumberLengthTen.toString();
    }

    /**
     * Given accountNumber may contain any alphanumeric character.
     * 
     * @param accountNumber String - the raw accountNumber to be checked
     * @return boolean - true if 11-check succeeds, false otherwise
     */
    public static boolean execute(String accountNumber) {
        // Calculate 11-check sum (left-to-right)
        String[] digitsLeftToRight = scrub(accountNumber).split("");
        Integer sumLeftToRight = 0;
        Integer positionLeftToRight = 1;
        for (String stringDigit : digitsLeftToRight) {
            Integer digit = Integer.parseInt(stringDigit);
            sumLeftToRight += digit * positionLeftToRight;
            positionLeftToRight++;
        }
        // Check if sum is dividable by 11
        return sumLeftToRight % 11 == 0;
    }

}
