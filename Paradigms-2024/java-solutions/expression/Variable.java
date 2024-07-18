package expression;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Variable implements Expressions{
    private String var;
    private int ind;

    public Variable(String var) {
        this.var = var;
    }
    public Variable(int num) {
        this.ind = num;
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public int hashCode() {
        return var.hashCode();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (var) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalStateException();
        };
    }
    @Override
    public int evaluate(List<Integer> vals) {
        return vals.get(ind);
    }

    public void setName(String name) {
        this.var = name;
    }

    @Override
    public int getPrior() {
        return 0;
    }

    public int count() {
        return 0;
    }

    @Override
    public int evaluate(int num) {
        return num;
    }

    @Override
    public BigDecimal evaluate(BigDecimal num) {
        return num;
    }


    @Override
    public boolean equals(Object other) {
        if (other != null && this.getClass() == other.getClass()) {
            return Objects.equals(var, ((Variable) other).var);
        }
        return false;
    }
}
