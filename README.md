# Quality workshop: refactoring an existing codebase
Contains exercises on refactoring an existing codebase

We are going to refactor an existing codebase.
The main functionality of this code lies in the AccountService. This service handles transaction requests which will be checked and executed.

We will be taking several refactor steps. After each step we can verify if the software still does what it has to do by running the AccountServiceTest and ElevenCheckTest.
This is also the main lesson of this exercise: you can never start refactoring existing code without a proper unittest base!

## Refactor step 1: delegation
