import java.util.ArrayList;
import java.util.List;

/**
 * Clase parser
 */

class Parser {
    private List<Token> tokens;
    private ArrayList<SyntaxException> errors;
    private int currentPosition = 0;

    // Constructores y metódos
    public Parser(List<Token> tokens, ArrayList<SyntaxException> errors) {
        this.tokens = tokens;
        this.errors = errors;
    }

    public void parse()  throws SyntaxException{
        while (!isAtEnd()) {
            try {
                parseStatement();
            } catch (SyntaxException e) {
                errors.add(e); // Add error to the list and continue parsing
                //advance();
                synchronize();
            }
        }
    }

    private void synchronize() {
        while (!isAtEnd()) {

            // Check if the current token is a good point to resume
            if (isAtStatementBoundary(peek())) {
                return; // Found a good point to resume
            }

            advance(); // Continue to the next token
        }
    }

    private boolean isAtStatementBoundary(Token token) {
        // Check if the token is a boundary of a statement
        if (token.getLexeme() == null) {
            return false; // Handle null lexeme
        }
        switch (token.getLexeme()) {
            case IF:
            case WHILE:
                return true;
            default:
                return false;
        }
    }


    private void parseStatement()  throws SyntaxException {
        Token currentToken = peek();
        if (currentToken.getType() == Token.Type.VARIABLE) {
            parseAssignmentStatement();
        } else {
            switch (currentToken.getLexeme()) {
                case WHILE -> parseWhileStatement();
                case IF -> parseIfStatement();
                default -> throw new SyntaxException("Syntax error: Unexpected token " + currentToken.getValue() + " at line " + currentToken.getLineNumber());
            }
        }
    }
    private void parseAssignmentStatement()  throws SyntaxException {
        Token variableToken = consume(Token.Type.VARIABLE); // Consumir el nombre de la variable
        consume(Token.Lexeme.ASIGN); // Consumir el operador '='
        Expression expression = parseExpression(); // Parsear la expresión a la derecha del '='
        consume(Token.Lexeme.SEMICOLON); // Consumir el ';'
        System.out.println("Valid assignment statement");
    }

    private Expression parseExpression() throws SyntaxException {
        Token firstToken = advance(); // Avanza al siguiente token

        // Checa que sea una operación matemática
        if (peek().getLexeme() == Token.Lexeme.PLUS || peek().getLexeme() == Token.Lexeme.MINUS ||
                peek().getLexeme() == Token.Lexeme.MULTIPLICATION || peek().getLexeme() == Token.Lexeme.DIVISION) {

            Token operator = advance();
            Token secondToken = advance();

            return new Expression.Binary(new Expression.Literal(firstToken), operator, new Expression.Literal(secondToken));
        } else {

            return new Expression.Literal(firstToken);
        }
    }


    private Condition parseCondition()  throws SyntaxException {
        Expression leftOperand = parseExpression(); //Comprueba que el operando de la izquierda sea una expresión

        // Se aegura de que el siguiente token es un operador
        Token operator;
        if (peek().getLexeme() == Token.Lexeme.LESS_THAN || peek().getLexeme() == Token.Lexeme.LESS_EQUAL_THAN ||
                peek().getLexeme() == Token.Lexeme.GREATER_THAN || peek().getLexeme() == Token.Lexeme.GRATER_EQUAL_THAN ||
                peek().getLexeme() == Token.Lexeme.EQUAL_TO) {

            operator = advance();
        } else {
            throw new SyntaxException("Expected comparison operator" + peek().getValue() + " at line " + peek().getLineNumber()); // Error si no es un operador
        }

        Expression rightOperand = parseExpression(); // Comprueba que el operando de la derecha sea una expresión

        return new Condition(leftOperand, operator, rightOperand);
    }



    private void parseIfStatement()  throws SyntaxException {
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
        System.out.println("Valid if then statement");
    }


    private void parseWhileStatement()  throws SyntaxException {
        consume(Token.Lexeme.WHILE); // Consumir 'while'
        Condition condition = parseCondition(); // Parsear condición
        consume(Token.Lexeme.DO); // Consumir 'do'

        while (!check(Token.Lexeme.ENDWHILE)) {
            parseStatement(); // Parsear cada sentencia dentro del bucle
        }

        consume(Token.Lexeme.ENDWHILE); // Consumir 'endwhile'
        System.out.println("Valid while statement");
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

    private Token consume(Token.Lexeme expectedLexeme)  throws SyntaxException {
        if (check(expectedLexeme)) {
            return advance();
        }
        throw new SyntaxException("Expected " + expectedLexeme + " but found " + peek().getType() + " at line " + peek().getLineNumber());
    }
    private Token consume(Token.Type expectedType) throws SyntaxException {
        if (checkType(expectedType)) {
            return advance();
        }
        throw new SyntaxException("Expected " + expectedType + " but found " + peek().getType() + " at line " + peek().getLineNumber());
    }

    private boolean checkType(Token.Type type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }
}
