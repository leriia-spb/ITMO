package expression.exceptions;

import expression.CalculationError;
import expression.Expressions;
import expression.UnaryOperation;

import java.math.BigDecimal;
import java.util.List;

public class CheckedNegate extends UnaryOperation {
    public CheckedNegate(Expressions arg) {
        super(arg);
    }

    @Override
    public int evaluate(int x, int y, int z) throws CalculationError {
        int res = arg.evaluate(x, y, z);
        if (res == Integer.MIN_VALUE){
            throw new CalculationError("Overflow");
        }
        return -res;
    }
    @Override
    public int evaluate(List<Integer> vars) throws CalculationError {
        int res = arg.evaluate(vars);
        if (res == Integer.MIN_VALUE){
            throw new CalculationError("Overflow");
        }
        return -res;
    }

    @Override
    public int evaluate(int num) {
        return -arg.evaluate(num);
    }

    @Override
    public BigDecimal evaluate(BigDecimal num) {
        return arg.evaluate(num).multiply(new BigDecimal(-1));
    }

    @Override
    public String getType() {
        return "-";
    }

    @Override
    public String toMiniString() {
        if (arg.count() == 2) {
            return String.format("-(%s)", arg.toMiniString());
        }
        return String.format("- %s", arg.toMiniString());
    }

}
