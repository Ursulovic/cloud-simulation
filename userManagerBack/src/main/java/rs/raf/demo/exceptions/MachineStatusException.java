package rs.raf.demo.exceptions;

public class MachineStatusException extends RuntimeException{
    public MachineStatusException(String message) {
        super(message);
    }
}
