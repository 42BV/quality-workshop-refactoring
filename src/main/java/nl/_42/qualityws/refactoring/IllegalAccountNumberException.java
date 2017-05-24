package nl._42.qualityws.refactoring;

public class IllegalAccountNumberException extends RuntimeException {

    public IllegalAccountNumberException(String message) {
        super("Illegal account: "+ message);
    }

}
