package expression;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


public class Const implements Expressions {
    private final BigDecimal bigDecNum;

    public Const(int num) {
        this.bigDecNum = BigDecimal.valueOf(num);
    }

    public Const(BigDecimal num) {
        this.bigDecNum = num;
    }

    @Override
    public String toString() {
        return bigDecNum.toString();
    }

    @Override
    public int hashCode() {
        return bigDecNum.hashCode();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return bigDecNum.intValue();
    }

    @Override
    public int evaluate(List<Integer> names) {
        return bigDecNum.intValue();
    }

    @Override
    public int evaluate(int n) {
        return bigDecNum.intValue();
    }

    public int count() {
        return 0;
    }

    public int getPrior(){
        return 0;
    }

    @Override
    public BigDecimal evaluate(BigDecimal n) {
        return bigDecNum;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && this.getClass() == other.getClass()) {
            return Objects.equals(bigDecNum, ((Const) other).bigDecNum);
        }
        return false;
    }
}
