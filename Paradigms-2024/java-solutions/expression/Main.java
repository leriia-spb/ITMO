package expression;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0) {
            // x^2−2x+1👀
            System.out.println(new Add(new Subtract(new Multiply(new Variable("x"), new Variable("x")),
                    new Multiply(new Const(2),
                            new Variable("x"))), new Const(1)).evaluate(Integer.parseInt(args[0])));
            System.out.println(new Divide(new Variable("x"), new Variable("x")).evaluate(new BigDecimal("0.12312")));
        }
    }
}
