package expression;

import java.math.BigDecimal;
import java.util.List;

public abstract class Operation implements Expressions {
    protected final Expressions arg1;
    protected final Expressions arg2;

    public Operation(Expressions arg1, Expressions arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String toString() {
        return String.format("(%s %s %s)", arg1.toString(), getType(), arg2.toString());
    }

    public int hashCode() {
        int h1 = arg1.hashCode();
        int h2 = arg2.hashCode();
        return h1 * 31 * 31 + getType().hashCode() + h2 * 31;
    }

    public abstract int doOperation(int num1, int num2);

    public abstract String getType();

    public abstract BigDecimal doOperation(BigDecimal num1, BigDecimal num2);

    @Override
    public int evaluate(int x, int y, int z){
        int num1 = arg1.evaluate(x, y, z);
        int num2 = arg2.evaluate(x, y, z);
        return doOperation(num1, num2);
    }
    @Override
    public int evaluate(List<Integer> vars){
        int num1 = arg1.evaluate(vars);
        int num2 = arg2.evaluate(vars);
        return doOperation(num1, num2);
    }

    public int count() {
        return 2;
    }

    @Override
    public int evaluate(int num){
        int num1 = arg1.evaluate(num);
        int num2 = arg2.evaluate(num);
        return doOperation(num1, num2);
    }

    @Override
    public BigDecimal evaluate(BigDecimal num) {
        BigDecimal num1 = arg1.evaluate(num);
        BigDecimal num2 = arg2.evaluate(num);
        return doOperation(num1, num2);
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && this.getClass() == other.getClass()) {
            return this.arg1.equals(((Operation) other).arg1) && arg2.equals(((Operation) other).arg2);
        }
        return false;
    }
}
