public abstract class Expression {

    public static class Literal extends Expression {

        public Literal(Token value) {
        }

        // Getter and other methods...
    }
    /**
     * Clase para representar una expresión binaria
     */
    public static class Binary extends Expression {
        private final Expression left;
        private final Expression right;
        private final Token operator;

        public Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        // Getter and other methods...
    }

    // Other expression types (like Unary, Grouping, etc.)...
}
