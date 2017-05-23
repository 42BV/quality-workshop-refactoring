package nl._42.qualityws.refactoring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import nl._42.qualityws.refactoring.domain.Account;
import nl._42.qualityws.refactoring.domain.AccountNumber;

@Repository
public class AccountRepository {

    private Map<AccountNumber, Account> accounts = new HashMap<>();

    public void clear() {
        accounts.clear();
    }

    public Account findByNumber(AccountNumber accountNumber) {
        return accounts.get(accountNumber);
    }

    public Account save(Account account) {
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

}
