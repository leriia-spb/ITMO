package expression.parser;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args){
        System.out.println(new ExpressionParser().parse("((1)").evaluate(0, 0, 0));
    }
}
