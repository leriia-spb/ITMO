package expression;

public class CalculationError extends RuntimeException {
    public CalculationError(String message) {
        super(message);
    }
}