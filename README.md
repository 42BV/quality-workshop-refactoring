# Quality workshop: refactoring an existing codebase
Contains the solution to the refactoring assignment.

## Refactor step 1: delegation to domain class
Apart from the first null check in the AccountService.transferAmount(..) method, there are 5 argument checks that can be delegated to a domain class...
- Move these 5 checks to the corresponding domain class. 
  - Moved the 5 checks to TransactionRequest constructor.
  - Moved the 3 exceptions to the domain package

## Refactor step 2: delegation to repository
When the AccountService uses the AccountRepository to find an Account for an AccountNumber, it has to check if the returned Account is not null each time it does such a lookup!
- Move the nullcheck into the repository.
  - AccountRepository.findByNumber(..) extended with null check.
  - Removed null check on Account from AccountService.transfer(..).

## Refactor step 3: delegation to private method
The last 3 code blocks (marked by comment) in the AccountService.transfer(..) mehtod can be pushed to their own private methods:
- verifyTransactionLimit(..)
- verifyDebtLimit(..)
- executeTransaction(..)
Take good care of what arguments and return types these 3 methods will get.  
When you are done and all unittests succeed, you can remove the comments that came with the 3 code blocks as the methods names are self-explaining now!
  - Extracted 3 code blocks into private mehtods on AccountService.
  - verifyDebtLimit(..) gets void return type as it must concentrate on one purpose, the verification, it's not responsible for determining the resultBalance of the from account.
  - All 3 private methods get BigDecimal amount as first argument instead of the complete TransactionRequest as only the amount is used within these methods.

## Refactor step 4: implementing equals/hashcode on domain class
In the verifyTransactionLimit(..) a check is done if the AccountHolder's are the same or not. This is done by comparing their name property. If we implement an equals/hashCode method pair (as is done in AccountNumber), we can call equals on AccountHolder directly.
- Implement the equals and hashCode methods on AccountHolder and use the equals method directly.
  - See AccountHolder equals and hashCode implementation and the usage of it in AccountService.verifyTransactionLimit(..).

## Refactor step 5: implementing inheritance
The enum AccountType has two values: NORMAL and PREMIUM. Whithin the verifyDebtLimit(..) method, the debt limit is derived by checking the AccountType.
It would be nice if we could ask a value of AccountType it's own debt limit! Again an example of moving code to the place where it belongs!
- Define an abstract method getDebtLimit() on the AccountType enum and implement this method in the body of each of the 2 values.
  - NORMAL { //implementation of abstract method }, PREMIUM { //implementation of abstract method }; 
- Now use this in the AccountService.verifyDebtLimit(..) private method.
  - See AccountType getDebtLimit implementations, moved constants from AccountService to AccountType where they belong.
  
The same can be done for transaction limit:
- Define an abstract method getTransactionLimit() on the AccountType enum and implement this method in the body of each of the 2 values.
- Now use this in the AccountService.verifyTransactionLimit(..) private method.
  - Note that determining OWN_TRANSACTION_LIMIT cannot be done by the enum!
  - See AccountType getTransactionLimit implementations, moved constants from AccountService to AccountType where they belong.

## BONUS EXERCISE: Refactor step 6: New requirement results in refactoring the AccountNumber
The AccountNumber class wraps the raw String accountNumber. In the equals/hashCode methods this raw String is used.  
So the accountNumber 73.61.60.221 is defined not equal to the accountNumber 0736160221. 
Now we would like to implement the requirement that these 2 AccountNumbers are seen as equal.
- We can do this by using the ElevenCheck util in the AccountNumber constructor to scrub the given raw String and store that instead.
  - See AccountNumber constructor
- Instead of a raw() method on AccountNumber we can implement a getter for the scrubbed accountNumber property.
  - See AccountNumber.getAccountNumber()
- Before the scrubbing we can 11-check the raw given number within the AccountNumber constructor.
  - See AccountNumber constructor
- Some (parts of) the account number checks in the TransactionRequest constructor can now be simplified.
  - See TransactionRequest constructor
- The AccountServiceTest is failing now because of the static INVALID_ACCOUNT, remove this constant from the test with the 2 test methods using it.
  - Removed constant and AccountServiceTest.transferAmount_throwsException_invalidFromAccount() and AccountServiceTest.transferAmount_throwsException_invalidToAccount()
- Implement an AccountNumberTest to test the new AccountNumber constructor with this INVALID_ACCOUNT number.
  - See AccountNumberTest.construct_shouldFail_withInvalidNumber()
- Add a test method to AccountNumberTest to test if 2 instances of AccountNumber constructed with 73.61.60.221 and 0736160221 are equal.
  - See AccountNumberTest tests for the equals method.