package expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class High extends UnaryOperation {
    public High(Expressions arg) {
        super(arg);
    }
    private int high(int num){
        if (num == 0){
            return 0;
        }
        BigInteger n = BigInteger.valueOf(num);
        int i = 0;
        int ans = 0;
        while (i < 32) {
            if (n.testBit(i)) {
                ans = i;
            }
            i++;
        }
        return 1 << ans;
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
        BigDecimal rez = arg.evaluate(num);
        return rez;
    }

    @Override
    public String getType() {
        return "high";
    }

    @Override
    public String toMiniString() {
        if (arg.count() == 2) {
            return String.format("high(%s)", arg.toMiniString());
        }
        return String.format("high %s", arg.toMiniString());
    }

}