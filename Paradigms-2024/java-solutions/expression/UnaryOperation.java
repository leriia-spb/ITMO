package expression;

import java.math.BigDecimal;

public abstract class UnaryOperation implements Expressions {
    protected final Expressions arg;

    public UnaryOperation(Expressions arg) {
        this.arg = arg;
    }

    public String toString() {
        return String.format("%s(%s)", getType(), arg.toString());
    }

    public int hashCode() {
        return getType().hashCode() + arg.hashCode() * 31;
    }

    public int count() {
        return 1;
    }

    public int getPrior(){
        return 0;
    }
    public String getType() {
        return "";
    }

    public BigDecimal doOperation(BigDecimal num1, BigDecimal num2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && this.getClass() == other.getClass()) {
            return this.arg.equals(((UnaryOperation) other).arg);
        }
        return false;
    }
}
