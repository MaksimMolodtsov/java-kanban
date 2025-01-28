package exceptions;

public class ManagerReadException extends RuntimeException {

    public ManagerReadException(final String message) {
        super(message);
    }

}
