package expression;

public class ParsingError extends RuntimeException {
    public ParsingError(String message) {
        super(message);
    }
}
