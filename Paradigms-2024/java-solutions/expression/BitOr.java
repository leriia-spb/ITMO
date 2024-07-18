package expression;
import java.math.BigDecimal;

public class BitOr extends Operation {
    public BitOr(Expressions arg1, Expressions arg2) {
        super(arg1, arg2);
    }

    @Override
    public int doOperation(int num1, int num2) {
        return num1 | num2;
    }

    @Override
    public BigDecimal doOperation(BigDecimal num1, BigDecimal num2) {
        return num1.add(num2);
    }

    @Override
    public String getType() {
        return "|";
    }

    public int getPrior(){
        return 5;
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
        return String.format("%s | %s", ans1, ans2);
    }
}
