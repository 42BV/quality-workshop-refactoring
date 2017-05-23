package nl._42.qualityws.refactoring;

import static nl._42.qualityws.refactoring.AccountService.NORMAL_DEBT_LIMIT;
import static nl._42.qualityws.refactoring.AccountService.NORMAL_TRANSACTION_LIMIT;
import static nl._42.qualityws.refactoring.AccountService.OWN_ACCOUNT_TRANSACTION_LIMIT;
import static nl._42.qualityws.refactoring.AccountService.PREMIUM_DEBT_LIMIT;
import static nl._42.qualityws.refactoring.AccountService.PREMIUM_TRANSACTION_LIMIT;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import nl._42.qualityws.refactoring.domain.Account;
import nl._42.qualityws.refactoring.domain.AccountHolder;
import nl._42.qualityws.refactoring.domain.AccountNumber;
import nl._42.qualityws.refactoring.domain.AccountType;
import nl._42.qualityws.refactoring.domain.Transaction;
import nl._42.qualityws.refactoring.domain.TransactionRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    public static AccountNumber INVALID_ACCOUNT         = new AccountNumber("11.11.11.111");
    public static AccountNumber VALID_FROM_ACCOUNT      = new AccountNumber("73.61.60.221");
    public static AccountNumber VALID_FROM_ACCOUNT_SAME = new AccountNumber("0736160221");
    public static AccountNumber VALID_TO_ACCOUNT        = new AccountNumber("126754365");

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @After
    public void clear() {
        accountRepository.clear();
    }

    @Test
    public void transferAmount_succeeds_transferSmallAmount() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", new BigDecimal("1000.00"), AccountType.NORMAL);
        createAccount(VALID_TO_ACCOUNT, "Eefje", new BigDecimal("1000.00"), AccountType.NORMAL);
        Transaction transaction = accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("42.00")));
        assertEquals("Aatje", transaction.getFrom().getHolder().getName());
        assertEquals("Eefje", transaction.getTo().getHolder().getName());
        assertEquals(new BigDecimal("42.00"), transaction.getAmount());
        assertEquals(new BigDecimal("958.00"), transaction.getFrom().getBalance());
        assertEquals(new BigDecimal("1042.00"), transaction.getTo().getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void transferAmount_throwsException_emptyTransactionRequest() {
        accountService.transfer(null);
    }

    @Test(expected = IllegalAccountNumberException.class)
    public void transferAmount_throwsException_emptyFromAccountNumber() {

        accountService.transfer(new TransactionRequest(
                null,
                VALID_TO_ACCOUNT,
                new BigDecimal("42.00")));
    }

    @Test(expected = IllegalAccountNumberException.class)
    public void transferAmount_throwsException_emptyToAccountNumber() {
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                null,
                new BigDecimal("42.00")));
    }

    @Test(expected = IllegalAccountNumberException.class)
    public void transferAmount_throwsException_invalidFromAccount() {
        accountService.transfer(new TransactionRequest(
                INVALID_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("42.00")));
    }

    @Test(expected = IllegalAccountNumberException.class)
    public void transferAmount_throwsException_invalidToAccount() {
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                INVALID_ACCOUNT,
                new BigDecimal("42.00")));
    }

    @Test(expected = IllegalAmountException.class)
    public void transferAmount_throwsException_amountWithoutDigits() {
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("42")));
    }

    @Test(expected = IllegalAmountException.class)
    public void transferAmount_throwsException_negativeAmount() {
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("-42.00")));
    }

    @Test(expected = SameAccountTransferException.class)
    public void transferAmount_throwsException_sameAccount() {
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_FROM_ACCOUNT_SAME,
                new BigDecimal("42.00")));
    }

    @Test(expected = AccountDoesNotExistException.class)
    public void transferAmount_throwsException_fromAccountDoesNotExist() {
        createAccount(VALID_TO_ACCOUNT, "Aatje", new BigDecimal("1000.00"), AccountType.NORMAL);
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("42.00")));
    }

    @Test(expected = AccountDoesNotExistException.class)
    public void transferAmount_throwsException_toAccountDoesNotExist() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", new BigDecimal("1000.00"), AccountType.NORMAL);
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("42.00")));
    }

    @Test
    public void transferAmount_succeeds_toOwnAccount_withinTransactionLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", OWN_ACCOUNT_TRANSACTION_LIMIT, AccountType.NORMAL);
        createAccount(VALID_TO_ACCOUNT, "Aatje", new BigDecimal("1000.00"), AccountType.NORMAL);
        Transaction transaction = accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                OWN_ACCOUNT_TRANSACTION_LIMIT));
        assertEquals(new BigDecimal("0.00"), transaction.getFrom().getBalance());
        assertEquals(new BigDecimal("21000.00"), transaction.getTo().getBalance());
    }

    @Test
    public void transferAmount_succeeds_fromPremiumAccount_withinTransactionLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", PREMIUM_TRANSACTION_LIMIT, AccountType.PREMIUM);
        createAccount(VALID_TO_ACCOUNT, "Eefje", new BigDecimal("1000.00"), AccountType.NORMAL);
        Transaction transaction = accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                PREMIUM_TRANSACTION_LIMIT));
        assertEquals(new BigDecimal("0.00"), transaction.getFrom().getBalance());
        assertEquals(new BigDecimal("11000.00"), transaction.getTo().getBalance());
    }

    @Test
    public void transferAmount_succeeds_fromNormalAccount_withinTransactionLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", NORMAL_TRANSACTION_LIMIT, AccountType.NORMAL);
        createAccount(VALID_TO_ACCOUNT, "Eefje", new BigDecimal("1000.00"), AccountType.NORMAL);
        Transaction transaction = accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                NORMAL_TRANSACTION_LIMIT));
        assertEquals(new BigDecimal("0.00"), transaction.getFrom().getBalance());
        assertEquals(new BigDecimal("6000.00"), transaction.getTo().getBalance());
    }

    @Test(expected = AmountExceedsTransactionLimitException.class)
    public void transferAmount_throwsException_toOwnAccount_exceedsTransactionLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", OWN_ACCOUNT_TRANSACTION_LIMIT, AccountType.NORMAL);
        createAccount(VALID_TO_ACCOUNT, "Aatje", new BigDecimal("1000.00"), AccountType.NORMAL);
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("20001.00")));
    }

    @Test(expected = AmountExceedsTransactionLimitException.class)
    public void transferAmount_throwsException_fromPremiumAccount_exceedsTransactionLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", PREMIUM_TRANSACTION_LIMIT, AccountType.PREMIUM);
        createAccount(VALID_TO_ACCOUNT, "Eefje", new BigDecimal("1000.00"), AccountType.NORMAL);
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("10001.00")));
    }

    @Test(expected = AmountExceedsTransactionLimitException.class)
    public void transferAmount_throwsException_fromNormalAccount_exceedsTransactionLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", NORMAL_TRANSACTION_LIMIT, AccountType.NORMAL);
        createAccount(VALID_TO_ACCOUNT, "Eefje", new BigDecimal("1000.00"), AccountType.NORMAL);
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("5001.00")));
    }

    @Test
    public void transferAmount_succeeds_toPremiumAccount_withinDebtLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", new BigDecimal("-9000.00"), AccountType.PREMIUM);
        createAccount(VALID_TO_ACCOUNT, "Eefje", new BigDecimal("1000.00"), AccountType.NORMAL);
        Transaction transaction = accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("1000.00")));
        assertEquals(PREMIUM_DEBT_LIMIT, transaction.getFrom().getBalance());
        assertEquals(new BigDecimal("2000.00"), transaction.getTo().getBalance());
    }

    @Test
    public void transferAmount_succeeds_toNormalAccount_withinTransactionLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", new BigDecimal("-1000.00"), AccountType.NORMAL);
        createAccount(VALID_TO_ACCOUNT, "Eefje", new BigDecimal("1000.00"), AccountType.NORMAL);
        Transaction transaction = accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("1000.00")));
        assertEquals(NORMAL_DEBT_LIMIT, transaction.getFrom().getBalance());
        assertEquals(new BigDecimal("2000.00"), transaction.getTo().getBalance());
    }

    @Test(expected = AmountExceedsDebtLimitException.class)
    public void transferAmount_throwsException_toPremiumAccount_exceedsDebtLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", PREMIUM_DEBT_LIMIT, AccountType.PREMIUM);
        createAccount(VALID_TO_ACCOUNT, "Eefje", new BigDecimal("1000.00"), AccountType.NORMAL);
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("0.01")));
    }

    @Test(expected = AmountExceedsDebtLimitException.class)
    public void transferAmount_throwsException_toNormalAccount_exceedsTransactionLimit() {
        createAccount(VALID_FROM_ACCOUNT, "Aatje", NORMAL_DEBT_LIMIT, AccountType.NORMAL);
        createAccount(VALID_TO_ACCOUNT, "Eefje", new BigDecimal("1000.00"), AccountType.NORMAL);
        accountService.transfer(new TransactionRequest(
                VALID_FROM_ACCOUNT,
                VALID_TO_ACCOUNT,
                new BigDecimal("0.01")));
    }

    private Account createAccount(AccountNumber accountNumber, String name, BigDecimal balance, AccountType type) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(balance);
        account.setHolder(createHolder(name));
        account.setType(type);
        return accountRepository.save(account);
    }

    private AccountHolder createHolder(String name) {
        AccountHolder holder = new AccountHolder();
        holder.setName(name);
        return holder;
    }


}
