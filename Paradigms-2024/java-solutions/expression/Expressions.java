package expression;

public interface Expressions extends Expression, TripleExpression, BigDecimalExpression, ListExpression {
    int count();

    int getPrior();
}
