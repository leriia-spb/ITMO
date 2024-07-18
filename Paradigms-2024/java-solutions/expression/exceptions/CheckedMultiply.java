package expression.exceptions;

import expression.CalculationError;
import expression.Operation;
import expression.Expressions;

import java.math.BigDecimal;

public class CheckedMultiply extends Operation {
    public CheckedMultiply(Expressions arg1, Expressions arg2) {
        super(arg1, arg2);
    }

    @Override
    public int doOperation(int num1, int num2) throws CalculationError {
        int res = num1 * num2;
        if (num1 == -1){
            if (num2 == Integer.MIN_VALUE){
                throw new CalculationError("Overflow");
            }
        }
        else if(num2 == -1){
            if (num1 == Integer.MIN_VALUE){
                throw new CalculationError("Overflow");
            }
        }
        else {
            if (num1 > 0 && num2 > 0 && num1 > Integer.MAX_VALUE / num2) {
                throw new CalculationError("Overflow");
            }
            if (num1 < 0 && num2 < 0 && num1 < Integer.MAX_VALUE / num2) {
                throw new CalculationError("Overflow");
            }
            if ( num1 < 0 && num2 > 0 && num1 < Integer.MIN_VALUE / num2) {
                throw new CalculationError("Overflow");
            }
            if ( num1 > 0 && num2 < 0 && num1 > Integer.MIN_VALUE / num2) {
                throw new CalculationError("Overflow");
            }

        }
        return res;
    }

    @Override
    public BigDecimal doOperation(BigDecimal num1, BigDecimal num2) {
        return num1.multiply(num2);
    }

    @Override
    public String getType() {
        return "*";
    }

    public int getPrior() {
        return 1;
    }

    @Override
    public String toMiniString() {
        String ans1 = arg1.toMiniString();
        String ans2 = arg2.toMiniString();
        if (arg1.getPrior() > getPrior()) {
            ans1 = String.format("(%s)", ans1);
        }
        if (arg2.getPrior() > getPrior() || arg2.getClass() == CheckedDivide.class) {
            ans2 = String.format("(%s)", ans2);
        }
        return String.format("%s * %s", ans1, ans2);
    }
}
