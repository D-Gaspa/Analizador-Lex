import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase parser
 */

class Parser {
    private final List<Token> tokens;
    private final ArrayList<SyntaxException> errors;
    private int currentPosition = 0;

    // Constructores y metódos
    public Parser(List<Token> tokens, ArrayList<SyntaxException> errors) {
        this.tokens = tokens;
        this.errors = errors;
    }

    /**
     * Método para iniciar el análisis sintáctico
     */
    public void parse()  throws SyntaxException{
        while (!isAtEnd()) {
            try {
                parseStatement();
            } catch (SyntaxException e) {
                errors.add(e); // Agrega error y continua con el analisis
                synchronize();
            }
        }
    }

    /**
     * Método para sincronizar el analizador sintáctico
     */
    private void synchronize() {
        while (!isAtEnd()) {
            // Chear un token de sincronización
            if (isAtStatementBoundary(Objects.requireNonNull(peek()))) {
                return; // Se encuentra un buen punto para retomar
            }

            advance(); // Continua al siguiente token
        }
    }

    /**
     * Método para checar si el token es un punto clave
     */
    private boolean isAtStatementBoundary(Token token) {
        // Checa si el token es un punto clave de otro token
        if (token.getLexeme() == null) {
            return false; // No es un token clave
        }
        return switch (token.getLexeme()) {
            case IF, WHILE -> true;
            default -> false;
        };
    }

    /**
     * Método para parsear una sentencia
     */
    private void parseStatement()  throws SyntaxException {
        Token currentToken = peek();
        assert currentToken != null;
        if (currentToken.getType() == Token.Type.VARIABLE) {
            parseAssignmentStatement();
        } else {
            switch (currentToken.getLexeme()) {
                case WHILE -> parseWhileStatement();
                case IF -> parseIfStatement();
                default -> throw new SyntaxException("Syntax error: Unexpected token " + currentToken.getValue() + " at line " + currentToken.getLineNumber(),  currentToken.getLineNumber());
            }
        }
    }

    /**
     * Método para parsear una asignación
     */
    private void parseAssignmentStatement()  throws SyntaxException {
        consume(Token.Type.VARIABLE);// Consumir el nombre de la variable
        consume(Token.Lexeme.ASSIGN); // Consumir el operador '='
        parseExpression(); // Parsear la expresión a la derecha del '='
        consume(Token.Lexeme.SEMICOLON); // Consumir el ';'

        System.out.println("Valid assignment statement at line " + previous().getLineNumber());
    }

    /**
     * Método para parsear una expresión matematica
     */
    private Expression parseExpression() {
        Token firstToken = advance(); // Avanza al siguiente token

        if (firstToken.getLexeme() == Token.Lexeme.ENDIF || firstToken.getLexeme() == Token.Lexeme.ENDWHILE) {
            return null;
        }

        // Checa que sea una operación matemática
        if (Objects.requireNonNull(peek()).getLexeme() == Token.Lexeme.PLUS || Objects.requireNonNull(peek()).getLexeme() == Token.Lexeme.MINUS ||
                Objects.requireNonNull(peek()).getLexeme() == Token.Lexeme.MULTIPLICATION || Objects.requireNonNull(peek()).getLexeme() == Token.Lexeme.DIVISION) {

            Token operator = advance();
            Token secondToken = advance();

            return new Expression.Binary(new Expression.Literal(firstToken), operator, new Expression.Literal(secondToken));
        } else {

            return new Expression.Literal(firstToken);
        }
    }

    /**
     * Método para parsear una condición
     */
    private Condition parseCondition()  throws SyntaxException {
        consume(Token.Lexeme.OPEN_PARENTHESIS);
        Expression leftOperand = parseExpression(); //Comprueba que el operando de la izquierda sea una expresión

        // Se asegura de que el siguiente token es un operador
        Token operator;
        if (Objects.requireNonNull(peek()).getLexeme() == Token.Lexeme.LESS_THAN || Objects.requireNonNull(peek()).getLexeme() == Token.Lexeme.LESS_EQUAL_THAN ||
                Objects.requireNonNull(peek()).getLexeme() == Token.Lexeme.GREATER_THAN || Objects.requireNonNull(peek()).getLexeme() == Token.Lexeme.GRATER_EQUAL_THAN ||
                Objects.requireNonNull(peek()).getLexeme() == Token.Lexeme.EQUAL_TO) {

            operator = advance();
        } else {
            throw new SyntaxException("Expected comparison operator" + Objects.requireNonNull(peek()).getValue() + " at line " + Objects.requireNonNull(peek()).getLineNumber(), Objects.requireNonNull(peek()).getLineNumber()); // Error si no es un operador
        }

        Expression rightOperand = parseExpression(); // Comprueba que el operando de la derecha sea una expresión
        consume(Token.Lexeme.CLOSURE_PARENTHESIS);

        return new Condition(leftOperand, operator, rightOperand);
    }


    /**
     * Método para parsear un bloque if-then-endif
     */
    private void parseIfStatement()  throws SyntaxException {
        consume(Token.Lexeme.IF); // Consumir 'if'
        parseCondition(); // Parsear condición
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
        System.out.println("Valid if then statement at line " + previous().getLineNumber());
    }

    /**
     * Método para parsear un bloque while-do  -endwhile
     */
    private void parseWhileStatement()  throws SyntaxException {
        consume(Token.Lexeme.WHILE); // Consumir 'while'
        parseCondition(); // Parsear condición
        consume(Token.Lexeme.DO); // Consumir 'do'

        while (!check(Token.Lexeme.ENDWHILE)) {
            parseStatement(); // Parsear cada sentencia dentro del bucle
        }

        consume(Token.Lexeme.ENDWHILE); // Consumir 'endwhile'
        System.out.println("Valid while statement at line " + previous().getLineNumber());
    }

    /**
     * Método para obtener el token actual
     */
    private Token peek() {
        if (isAtEnd()) return null;
        return tokens.get(currentPosition);
    }

    /**
     * Método para obtener el token anterior
     */
    private Token previous() {
        return tokens.get(currentPosition - 1);
    }

    /**
     * Método para checar si se ha llegado al final de la lista de tokens
     */
    private boolean isAtEnd() {
        return currentPosition >= tokens.size();
    }

    /**
     * Método para avanzar al siguiente token
     */
    private Token advance() {
        if (!isAtEnd()) currentPosition++;
        return previous();
    }

    /**
     * Método para checar si el token actual es del tipo especificado
     */
    private boolean check(Token.Lexeme lexeme) {
        if (isAtEnd()) return false;
        return Objects.requireNonNull(peek()).getLexeme() == lexeme;
    }

    /**
     * Método para checar si el token actual es del tipo especificado
     * @param expectedLexeme El lexema esperado
     */
    private Token consume(Token.Lexeme expectedLexeme)  throws SyntaxException {
        if (check(expectedLexeme)) {
            return advance();
        }
        if (peek() == null) {
            throw new SyntaxException("Expected " + expectedLexeme, 0);
        }
        throw new SyntaxException("Expected " + expectedLexeme + " but found " + Objects.requireNonNull(peek()).getType() + " at line " + Objects.requireNonNull(peek()).getLineNumber(), Objects.requireNonNull(peek()).getLineNumber());
    }


    /**
     * Método para checar si el token actual es del tipo especificado
     * @param type El tipo de token a checar
     */
    private boolean checkType(Token.Type type) {
        if (isAtEnd()) return false;
        return Objects.requireNonNull(peek()).getType() == type;
    }

    /**
     * Método para checar si el token actual es del tipo especificado
     * @param expectedType El tipo de token esperado
     */
    private Token consume(Token.Type expectedType) throws SyntaxException {
        if (checkType(expectedType)) {
            return advance();
        }
        throw new SyntaxException("Expected " + expectedType + " but found " + Objects.requireNonNull(peek()).getType() + " at line " + Objects.requireNonNull(peek()).getLineNumber(), Objects.requireNonNull(peek()).getLineNumber());
    }
}