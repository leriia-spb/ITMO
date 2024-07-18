package expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class Low extends UnaryOperation {
    public Low(Expressions arg) {
        super(arg);
    }

    private int low(int num) {
        if (num == 0) {
            return 0;
        }
        BigInteger n = BigInteger.valueOf(num);
        int ans = 0;
        while (ans < 32 && !n.testBit(ans)) {
            ans++;
        }
        return 1 << ans;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return low(arg.evaluate(x, y, z));
    }

    @Override
    public int evaluate(List<Integer> vars) {
        return low(arg.evaluate(vars));
    }

    @Override
    public int evaluate(int num) {
        return low(arg.evaluate(num));
    }

    @Override
    public BigDecimal evaluate(BigDecimal num) {
        return arg.evaluate(num).multiply(new BigDecimal(-1));
    }

    @Override
    public String getType() {
        return "low";
    }

    @Override
    public String toMiniString() {
        if (arg.count() == 2) {
            return String.format("low(%s)", arg.toMiniString());
        }
        return String.format("low %s", arg.toMiniString());
    }

}