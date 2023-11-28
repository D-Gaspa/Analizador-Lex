
public class Condition {
    private final Expression leftOperand;
    private final Token operator;
    private final Expression rightOperand;

    /**
     * Constructor de la clase Condition
     * @param leftOperand   Operando izquierdo de la condición
     * @param operator      Operador de la condición
     * @param rightOperand  Operando derecho de la condición
     */
    public Condition(Expression leftOperand, Token operator, Expression rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    // Obtener los operandos y el operador de la condición
    public Expression getLeftOperand() {
        return leftOperand;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }
}
