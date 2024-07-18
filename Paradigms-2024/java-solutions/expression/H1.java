package expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class H1 extends UnaryOperation {
    public H1(Expressions arg) {
        super(arg);
    }
    private int high(int num){
        BigInteger n = BigInteger.valueOf(num);
        int ans = 0;
        while (ans < 32 && n.testBit(ans)) {
            ans++;
        }
        return ans;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return high(arg.evaluate(x, y, z));
    }

    @Override
    public int evaluate(int num) {
        return high(arg.evaluate(num));
    }

    @Override
    public int evaluate(List<Integer> vars) {
        return high(arg.evaluate(vars));
    }

    @Override
    public BigDecimal evaluate(BigDecimal num) {
        return arg.evaluate(num).multiply(new BigDecimal(-1));
    }

    @Override
    public String getType() {
        return "t1";
    }

    @Override
    public String toMiniString() {
        if (arg.count() == 2) {
            return String.format("t1(%s)", arg.toMiniString());
        }
        return String.format("t1 %s", arg.toMiniString());
    }

}

