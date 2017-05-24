# Quality workshop: refactoring an existing codebase
Contains exercises on refactoring an existing codebase.

We are going to refactor an existing codebase. It's build with the Spring Dependency Injection framework.
The domain is bank account centric. The main functionality of this code lies in the AccountService. This service handles transaction requests which will be checked and executed.  
All money amounts in this domain are implicitly in euro currency. So that's why you won't find explicit Currency class usage within this domain.

We will be taking several refactor steps. After each step we can verify if the software still does what it has to do by running the AccountServiceTest and ElevenCheckTest.
This is also the main lesson of this exercise: you can never start refactoring existing code without a proper unittest base!

## Refactor step 1: delegation to domain class
Apart from the first null check in the AccountService.transferAmount(..) method, there are 5 argument checks that can be delegated to a domain class...
- Move these 5 checks to the corresponding domain class.

## Refactor step 2: delegation to repository
When the AccountService uses the AccountRepository to find an Account for a AccountNumber, it has to check if the returned Account is not null each time it does such a lookup!
- Move the nullcheck into the repository.

## Refactor step 3: delegation to private method
The last 3 code blocks (marked by comment) in the AccountService.transfer(..) mehtod can be pushed to their own private methods:
- verifyTransactionLimit(..)
- verifyDebtLimit(..)
- executeTransaction(..)
Take good care of what arguments and return types these 3 methods will get.  
When you are done and all unittests succeed, you can remove the comments that came with the 3 code blocks as the methods names are self-explaining now!

## Refactor step 4: implementing equals/hashcode on domain class
In the verifyTransactionLimit(..) a check is done if the AccountHolder's are the same or not. This is done by comparing their name property. If we implement an equals/hashCode method pair (as is done in AccountNumber), we can call equals on AccountHolder directly.
- Implement the equals and hashCode methods on AccountHolder and use the equals method directly.

## Refactor step 5: implementing inheritance
The enum AccountType has two values: NORMAL and PREMIUM. Whithin the verifyDebtLimit(..) method, the debt limit is derived by checking the AccountType.
It would be nice if we could ask a value of AccountType it's own debt limit! Again an example of moving code to the place where it belongs!
- Define an abstract method getDebtLimit() on the AccountType enum and implement this method in the body of each of the 2 values.
  - NORMAL { //implementation of abstract method }, PREMIUM { //implementation of abstract method }; 
- Now use this in the AccountService.verifyDebtLimit(..) private method.

The same can be done for transaction limit:
- Define an abstract method getTransactionLimit() on the AccountType enum and implement this method in the body of each of the 2 values.
- Now use this in the AccountService.verifyTransactionLimit(..) private method.
  - Note that determining OWN_TRANSACTION_LIMIT cannot be done by the enum!

## BONUS EXERCISE: Refactor step 6: New requirement results in refactoring the AccountNumber
The AccountNumber class wraps the raw String accountNumber. In the equals/hashCode methods this raw String is used.
So the accountNumber 73.61.60.221 is defined not equal to the accountNumber 0736160221. Now we would like to implement the requirement that these 2 AccountNumbers are seen as equal.
- We can do this by using the ElevenCheck util in the AccountNumber constructor to scrub the given raw String and store that instead.
- Instead of a raw() method on AccountNumber we can implement a getter for the scrubbed accountNumber property.
- Before the scrubbing we can 11-check the raw given number within the AccountNumber constructor.
- Some (parts of) the account number checks in the TransactionRequest constructor can now be simplified.
- The AccountServiceTest is failing now because of the static INVALID_ACCOUNT, remove this constant from the test with the 2 test methods using it.
- Implement an AccountNumberTest to test the new AccountNumber constructor with this INVALID_ACCOUNT number.
- Add a test method to AccountNumberTest to test if 2 instances of AccountNumber constructed with different raws but equal scubbed accountNumber (see example above) are equal.