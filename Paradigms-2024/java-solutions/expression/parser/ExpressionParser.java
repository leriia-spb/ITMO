package expression.parser;

import expression.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ExpressionParser implements TripleParser {
    public TripleExpression parse(final String source) {
        return new Parser(source).parseExpressionTriple();
    }

    protected static class Parser {
        private final String source;
        private final List<String> names;
        private int pos = 0;

        public Parser(final String source) {
            this.source = source;
            this.names = null;
        }

        public Parser(final String source, List<String> names) {
            this.source = source;
            this.names = names;
        }

        public TripleExpression parseExpressionTriple() throws ParsingError {
            final TripleExpression result = parse(6);
            if (pos != source.length()) {
                throw new ParsingError("Unknown symbol at " + (pos + 1) + " position");
            }
            return result;
        }

        public ListExpression parseExpressionList() throws ParsingError {
            final ListExpression result = parse(6);
            if (pos != source.length()) {
                throw new ParsingError("Unknown symbol at " + (pos + 1) + " position");
            }
            return result;
        }

        protected Expressions buildOperation(String operation, Expressions exp1, Expressions exp2) {
            // final Map<Character, BiFunction<CommonExpression, CommonExpression, CommonExpression>> operations = Map.of(
            //         '+', (a, b) -> new Add(a, b)
            // );

            return switch (operation) {
                case "+" -> new Add(exp1, exp2);
                case "-" -> new Subtract(exp1, exp2);
                case "/" -> new Divide(exp1, exp2);
                case "*" -> new Multiply(exp1, exp2);
                case "&" -> new BitAnd(exp1, exp2);
                case "^" -> new BitXor(exp1, exp2);
                case "|" -> new BitOr(exp1, exp2);
                case ">>" -> new BinRight(exp1, exp2);
                case "<<" -> new BinLeft(exp1, exp2);
                case ">>>" -> new Right(exp1, exp2);
                case "min" -> new Min(exp1, exp2);
                case "max" -> new Max(exp1, exp2);
                default -> throw new IllegalStateException("Unknown operation" + operation);
            };
        }

        protected Expressions buildUnaryOperation(String operation, Expressions exp) {
            return switch (operation) {
                case "-" -> new Negate(exp);
                case "low" -> new Low(exp);
                case "l1" -> new L1(exp);
                case "t1" -> new H1(exp);
                case "high" -> new High(exp);
                default -> throw new IllegalStateException("Unknown operation" + operation);
            };
        }

        protected int getLvl(String operation) {
            return switch (operation) {
                case "+", "-" -> 2;
                case "/", "*" -> 1;
                case "&" -> 3;
                case "^" -> 4;
                case "|" -> 5;
                case "min", "max", ">>", "<<", ">>>" -> 6;
                default -> 0;
            };
        }

        protected String getPair(String symb) {
            return switch (symb) {
                case "{" -> "}";
                case "(" -> ")";
                case "[" -> "]";
                default -> throw new IllegalStateException("Unknown type of brackets: " + symb);
            };
        }

        protected String getBinaryOperator() {
            skipWhitespace();
            String oper = source.substring(pos, pos + 1);
            if (getLvl(oper) > 0) {
                return oper;
            }
            if (source.startsWith(">>>", pos)) {
                return ">>>";
            }
            if (source.startsWith(">>", pos)) {
                return ">>";
            }
            if (source.startsWith("<<", pos)) {
                return "<<";
            }
            if (source.startsWith("min", pos)) {
                return "min";
            }
            if (source.startsWith("max", pos)) {
                return "max";
            }
            //
            // enum OperationType {
            //     ADD("+"), MIN("min"), NONE("");
            //
            //     private final String name;
            //
            //     OperationType(final String name) {
            //         this.name = name;
            //     }
            //
            //     public String getName() {
            //         return name;
            //     }
            // }
            //
            // OperationType.MIN.name.length()

            return "?";
        }

        protected String getUnaryOperator() {
            if (source.startsWith("-", pos) && pos + 1 < source.length()
                    && !between('0', '9', source.charAt(pos + 1))) {
                pos += 1;
                return "-";
            }
            if (source.startsWith("low", pos)) {
                pos += 3;
                return "low";
            }
            if (source.startsWith("l1", pos)) {
                pos += 2;
                return "l1";
            }
            if (source.startsWith("t1", pos)) {
                pos += 2;
                return "t1";
            }
            if (source.startsWith("high", pos)) {
                pos += 4;
                return "high";
            }
            return "?";
        }

        protected Expressions parse(int number) throws ParsingError {
            if (number == 0) {
                return parseUnary();
            }
            skipWhitespace();
            Expressions result = parse(number - 1);
            skipWhitespace();
            while (pos < source.length()) {
                String operator = getBinaryOperator();
                if (getLvl(operator) != number) {
                    break;
                } else {
                    if (number == 6 && Character.isDigit(source.charAt(pos - 1))) {
                        throw new ParsingError("Error in first argument at " + (pos + 1) + " position");
                    }
                    pos += operator.length();
                }
                skipWhitespace();
                if (number == 6 && Character.isDigit(source.charAt(pos - 1))) {
                    throw new ParsingError("Error in second argument at " + (pos + 1) + " position");
                }
                Expressions second = parse(number - 1);
                result = buildOperation(operator, result, second);
                skipWhitespace();
            }
            return result;
        }

        protected Expressions parseUnary() throws ParsingError {
            skipWhitespace();
            String oper = getUnaryOperator();
            if (oper.equals("?")) {
                return parseValue();
            }
            return buildUnaryOperation(oper, parseUnary());
        }

        protected Expressions parseValue() throws ParsingError {
            skipWhitespace();
            if (pos < source.length()) {
                String next = source.substring(pos, pos + 1);
                Expressions result;
                if (next.equals("(") || next.equals("{") || next.equals("[")) {
                    pos++;
                    result = parse(6);
                    String end;
                    if (pos < source.length()) {
                        end = source.substring(pos, pos + 1);
                    } else {
                        throw new ParsingError("Unexpected end of expression");
                    }
                    if (end.equals(getPair(next))) {
                        pos++;
                        return result;
                    }
                    throw new ParsingError(getPair(next) + " expected but received " + end);
                }
                StringBuilder value = new StringBuilder();
                if (next.equals("-")) {
                    value.append('-');
                    pos++;
                }
                if (pos < source.length()) {
                    if (names == null && (next.equals("x") || next.equals("y") || next.equals("z"))) {
                        value.append(next);
                        pos++;
                        return new Variable(value.toString());
                    } else if (names != null) {
                        String end = source.substring(pos);
                        for (int i = 0; i < names.size(); i++) {
                            if (end.startsWith(names.get(i))) {
                                pos += names.get(i).length();
                                Variable var = new Variable(i);
                                var.setName(names.get(i));
                                return var;
                            }
                        }
                    }
                    value.append(parseNumber());
                    try {
                        return new Const(Integer.parseInt(value.toString()));
                    } catch (NumberFormatException e) {
                        if (value.toString().isEmpty()) {
                            throw new ParsingError("Expected a number at position " + (pos + 1));
                        } else {
                            throw new ParsingError("Overflow for Const");
                        }
                    }
                }
                throw new ParsingError("Unexpected end of number " + value);
            }
            throw new ParsingError("Unexpected end of expression");
        }

        protected String parseNumber() {
            final StringBuilder sb = new StringBuilder();
            while (pos < source.length() && between('0', '9', source.charAt(pos))) {
                sb.append(source.charAt(pos));
                pos++;
            }
            return sb.toString();
        }

        protected boolean between(final char from, final char to, final char ch) {
            return from <= ch && ch <= to;
        }

        protected void skipWhitespace() {
            while (pos < source.length() && Character.isWhitespace(source.charAt(pos))) {
                pos++;
            }
        }
    }
}
