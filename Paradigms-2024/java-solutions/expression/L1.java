package expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class L1 extends UnaryOperation {
    public L1(Expressions arg) {
        super(arg);
    }

    private int low(int num) {
        BigInteger n = BigInteger.valueOf(num);
        int i = 0;
        int ans = 0;
        while (i < 32) {
            if(n.testBit(i)) {
                ans++;
            }
            else{
                ans = 0;
            }
            i++;
        }
        return ans;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return low(arg.evaluate(x, y, z));
    }

    @Override
    public int evaluate(int num) {
        return low(arg.evaluate(num));
    }
    @Override
    public int evaluate(List<Integer> vars) {
        return low(arg.evaluate(vars));
    }

    @Override
    public BigDecimal evaluate(BigDecimal num) {
        return arg.evaluate(num).multiply(new BigDecimal(-1));
    }

    @Override
    public String getType() {
        return "l1";
    }

    @Override
    public String toMiniString() {
        if (arg.count() == 2) {
            return String.format("l1(%s)", arg.toMiniString());
        }
        return String.format("l1 %s", arg.toMiniString());
    }

}
