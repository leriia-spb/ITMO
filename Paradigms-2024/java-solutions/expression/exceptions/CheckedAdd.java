package expression.exceptions;

import expression.CalculationError;
import expression.Expressions;
import expression.Operation;

import java.math.BigDecimal;

public class CheckedAdd extends Operation {
    public CheckedAdd(Expressions arg1, Expressions arg2) {
        super(arg1, arg2);
    }

    @Override
    public int doOperation(int num1, int num2) throws CalculationError {
        int res = num1 + num2;
        if (num1 > 0 && num2 > 0 && Integer.MAX_VALUE - num1 < num2){
            throw new CalculationError("Overflow");
        }
        if (num1 < 0 && num2 < 0 && Integer.MIN_VALUE - num1 > num2){
            throw new CalculationError("Overflow");
        }
        return res;
    }

    @Override
    public BigDecimal doOperation(BigDecimal num1, BigDecimal num2) {
        return num1.add(num2);
    }

    @Override
    public String getType() {
        return "+";
    }

    @Override
    public String toMiniString() {
        String ans1 = arg1.toMiniString();
        String ans2 = arg2.toMiniString();
        if (arg1.getPrior() > getPrior()) {
            ans1 = String.format("(%s)", ans1);
        }
        if (arg2.getPrior() > getPrior()) {
            ans2 = String.format("(%s)", ans2);
        }
        return String.format("%s + %s", ans1, ans2);
    }

    public int getPrior() {
        return 2;
    }
}
