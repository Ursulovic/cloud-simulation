package rs.raf.demo.exceptions;

public class ForbiddenException extends RuntimeException{

    public ForbiddenException(String message) {
        super(message);
    }
}
