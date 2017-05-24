package nl._42.qualityws.refactoring.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class AccountNumberTest {

    public static String INVALID_ACCOUNT = "11.11.11.111";
    
    @Test(expected = IllegalAccountNumberException.class)
    public void construct_shouldFail_withInvalidNumber() {
        new AccountNumber(INVALID_ACCOUNT);
    }
    
    @Test
    public void equals_shouldReturnTrue_withDifferentRawsAndSameScrubbed() {
        AccountNumber number1 = new AccountNumber("73.61.60.221");
        AccountNumber number2 = new AccountNumber("0736160221");
        
        assertEquals(number1, number2);
    }
    
    @Test
    public void equals_shouldReturnTrue_withSameNumbers() {
        AccountNumber number1 = new AccountNumber("73.61.60.221");
        AccountNumber number2 = new AccountNumber("73.61.60.221");
        
        assertEquals(number1, number2);
    }
    
    @Test
    public void equals_shouldReturnFalse_withDifferentNumbers() {
        AccountNumber number1 = new AccountNumber("73.61.60.221");
        AccountNumber number2 = new AccountNumber("390.1689.39");
        
        Assert.assertFalse(number1.equals(number2));
    }
}
