package expression;


import java.math.BigDecimal;
import java.util.List;

public class Negate extends UnaryOperation {
    public Negate(Expressions arg) {
        super(arg);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -arg.evaluate(x, y, z);
    }

    @Override
    public int evaluate(List<Integer> vars) {
        return -arg.evaluate(vars);
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
