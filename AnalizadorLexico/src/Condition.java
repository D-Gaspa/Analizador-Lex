public class Condition {
    private final Expression leftOperand;
    private final Token operator;
    private final Expression rightOperand;

    public Condition(Expression leftOperand, Token operator, Expression rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    // Getters for each field
    public Expression getLeftOperand() {
        return leftOperand;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    // You might also want to add a method to evaluate or process the condition
    // depending on how you plan to use this class.
}
