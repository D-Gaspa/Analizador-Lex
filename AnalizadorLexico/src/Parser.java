import java.util.List;

/**
 * Clase parser
 */

class Parser {
    private List<Token> tokens;
    private int currentPosition = 0;

    // Constructores y metódos
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!isAtEnd()) {
            parseStatement();
        }
    }

    private void parseStatement() {
        Token currentToken = peek();
        if (currentToken.getType() == Token.Type.VARIABLE) {
            parseAssignmentStatement();
        } else {
            switch (currentToken.getLexeme()) {
                case WHILE -> parseWhileStatement();
                case IF -> parseIfStatement();
                default -> throw new ParseException("Sytax error: unexpected token " + currentToken.getValue());
            }
        }
    }
    private void parseAssignmentStatement() {
        Token variableToken = consume(Token.Type.VARIABLE); // Consumir el nombre de la variable
        consume(Token.Lexeme.ASIGN); // Consumir el operador '='
        Expression expression = parseExpression(); // Parsear la expresión a la derecha del '='
    }

    private Expression parseExpression() {
        Token firstToken = advance(); // Advance without checking, expecting a number or variable

        // Simple check for a binary operation
        if (peek().getLexeme() == Token.Lexeme.PLUS || peek().getLexeme() == Token.Lexeme.MINUS ||
                peek().getLexeme() == Token.Lexeme.MULTIPLICATION || peek().getLexeme() == Token.Lexeme.DIVISION) {

            Token operator = advance(); // Operator token
            Token secondToken = advance(); // Next number or variable

            // Construct a binary expression
            return new Expression.Binary(new Expression.Literal(firstToken), operator, new Expression.Literal(secondToken));
        } else {
            // Single number or variable
            return new Expression.Literal(firstToken);
        }
    }


    private Condition parseCondition() {
        Expression leftOperand = parseExpression(); // Parse the left operand, which is an expression

        // Ensure the next token is a comparison operator
        Token operator;
        if (peek().getLexeme() == Token.Lexeme.LESS_THAN || peek().getLexeme() == Token.Lexeme.LESS_EQUAL_THAN ||
                peek().getLexeme() == Token.Lexeme.GREATER_THAN || peek().getLexeme() == Token.Lexeme.GRATER_EQUAL_THAN ||
                peek().getLexeme() == Token.Lexeme.EQUAL_TO) {

            operator = advance(); // Comparison operator token
        } else {
            throw new ParseException("Expected comparison operator");
        }

        Expression rightOperand = parseExpression(); // Parse the right operand, which is an expression

        return new Condition(leftOperand, operator, rightOperand);
    }



    private void parseIfStatement() {
        consume(Token.Lexeme.IF); // Consumir 'if'
        Condition condition = parseCondition(); // Parsear condición
        consume(Token.Lexeme.THEN); // Consumir 'then'

        while (!check(Token.Lexeme.ENDIF) && !check(Token.Lexeme.ELSE)) {
            parseStatement(); // Parsear sentencias del bloque 'then'
        }

        if (check(Token.Lexeme.ELSE)) {
            consume(Token.Lexeme.ELSE); // Consumir 'else'
            while (!check(Token.Lexeme.ENDIF)) {
                parseStatement(); // Parsear sentencias del bloque 'else'
            }
        }

        consume(Token.Lexeme.ENDIF); // Consumir 'endif'
    }


    private void parseWhileStatement() {
        consume(Token.Lexeme.WHILE); // Consumir 'while'
        Condition condition = parseCondition(); // Parsear condición
        consume(Token.Lexeme.DO); // Consumir 'do'

        while (!check(Token.Lexeme.ENDWHILE)) {
            parseStatement(); // Parsear cada sentencia dentro del bucle
        }

        consume(Token.Lexeme.ENDWHILE); // Consumir 'endwhile'
    }


    private Token peek() {
        return tokens.get(currentPosition);
    }

    private Token previous() {
        return tokens.get(currentPosition - 1);
    }

    private boolean isAtEnd() {
        return currentPosition >= tokens.size();
    }

    private Token advance() {
        if (!isAtEnd()) currentPosition++;
        return previous();
    }

    private boolean check(Token.Lexeme lexeme) {
        if (isAtEnd()) return false;
        return peek().getLexeme() == lexeme;
    }

    private Token consume(Token.Lexeme expectedLexeme) {
        if (check(expectedLexeme)) {
            return advance();
        }
        throw new ParseException("Expected " + expectedLexeme + " but found " + peek().getType());
    }
    private Token consume(Token.Type expectedType) {
        if (checkType(expectedType)) {
            return advance();
        }
        throw new ParseException("Expected " + expectedType + " but found " + peek().getType());
    }
    private boolean checkType(Token.Type type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    public class ParseException extends RuntimeException {
        public ParseException(String message) {
            super(message);
        }
    }
}
