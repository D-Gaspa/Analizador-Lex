public abstract class Expression {

    public static class Literal extends Expression {
        private final Token value;

        public Literal(Token value) {
            this.value = value;
        }

        // Getter and other methods...
    }

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
