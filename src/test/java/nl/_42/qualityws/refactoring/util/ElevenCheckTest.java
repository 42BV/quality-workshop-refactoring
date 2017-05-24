package nl._42.qualityws.refactoring.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ElevenCheckTest {
    
    @Test
    public void elevenCheck_shouldSucceed_whenAccountNumber_valid_padded_numericChars() {
        final String accountNumber = "0736160221";
        assertTrue(ElevenCheck.execute(accountNumber));
    }

    @Test
    public void elevenCheck_shouldSucceed_whenAccountNumber_valid_padded_mixedChars() {
        final String accountNumber = "073.61.60.221";
        assertTrue(ElevenCheck.execute(accountNumber));
    }

    @Test
    public void elevenCheck_shouldSucceed_whenAccountNumber_valid_unpadded_numericChars() {
        final String accountNumber = "736160221";
        assertTrue(ElevenCheck.execute(accountNumber));
    }

    @Test
    public void elevenCheck_shouldSucceed_whenAccountNumber_valid_unpadded_mixedChars() {
        final String accountNumber = "73.61.60.221";
        assertTrue(ElevenCheck.execute(accountNumber));
    }

    @Test
    public void elevenCheck_shouldFail_whenAccountNumber_invalid_padded_numericChars() {
        final String accountNumber = "0736160222";
        assertFalse(ElevenCheck.execute(accountNumber));
    }

    @Test
    public void elevenCheck_shouldFail_whenAccountNumber_invalid_padded_mixedChars() {
        final String accountNumber = "073.61.60.222";
        assertFalse(ElevenCheck.execute(accountNumber));
    }

    @Test
    public void elevenCheck_shouldFail_whenAccountNumber_invalid_unpadded_numericChars() {
        final String accountNumber = "736160222";
        assertFalse(ElevenCheck.execute(accountNumber));
    }

    @Test
    public void elevenCheck_shouldFail_whenAccountNumber_invalid_unpadded_mixedChars() {
        final String accountNumber = "73.61.60.222";
        assertFalse(ElevenCheck.execute(accountNumber));
    }

    @Test(expected = IllegalArgumentException.class)
    public void elevenCheck_shouldError_whenAccountNumber_null() {
        ElevenCheck.execute(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void elevenCheck_shouldError_whenAccountNumber_zeroesOnly() {
        final String accountNumber = "00000000";
        ElevenCheck.execute(accountNumber);
    }

    @Test(expected = IllegalArgumentException.class)
    public void elevenCheck_shouldError_whenAccountNumber_exceedsMaxLength() {
        final String accountNumber = "123456789012";
        ElevenCheck.execute(accountNumber);
    }
}
