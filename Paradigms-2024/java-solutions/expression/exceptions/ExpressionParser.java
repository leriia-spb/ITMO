package expression.exceptions;


import expression.*;

import java.util.List;

public class ExpressionParser extends expression.parser.ExpressionParser implements TripleParser, ListParser {
    public TripleExpression parse(final String source){
        return new MyParser(source).parseExpressionTriple();
    }
    public ListExpression parse(final String source, List<String> names){
        return new MyParser(source, names).parseExpressionList();
    }
    protected static class MyParser extends expression.parser.ExpressionParser.Parser {
        public MyParser(String source) {
            super(source);
        }
        public MyParser(String source, List<String> names) {
            super(source, names);
        }
        @Override
        protected Expressions buildUnaryOperation(String operation, Expressions exp) {
            return switch (operation) {
                case "-" -> new CheckedNegate(exp);
                case "low" -> new Low(exp);
                case "l1" -> new L1(exp);
                case "t1" -> new H1(exp);
                case "high" -> new High(exp);
                default -> throw new IllegalStateException("Unknown operation" + operation);
            };
        }

        @Override
        protected Expressions buildOperation(String operation, Expressions exp1, Expressions exp2) {
            return switch (operation) {
                case "+" -> new CheckedAdd(exp1, exp2);
                case "-" -> new CheckedSubtract(exp1, exp2);
                case "/" -> new CheckedDivide(exp1, exp2);
                case "*" -> new CheckedMultiply(exp1, exp2);
                case "<<" -> new BinLeft(exp1, exp2);
                case ">>" -> new BinRight(exp1, exp2);
                case ">>>" -> new Right(exp1, exp2);
                case "min" -> new Min(exp1, exp2);
                case "max" -> new Max(exp1, exp2);
                default -> throw new IllegalStateException("Unknown operation" + operation);
            };
        }
    }
}
