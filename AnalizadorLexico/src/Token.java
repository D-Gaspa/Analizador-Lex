/**
 * La clase Token representa un token individual identificado durante el análisis léxico.
 * Un token contiene información sobre su valor, tipo y lexema correspondiente.
 */
public class Token {
    private String value; // Valor del token (cadena exacta encontrada en el input)
    private Type type;    // Tipo del token (enum Type)
    private Lexeme lexeme; // Lexema asociado al token (enum Lexeme)
    private int lineNumber; // Número de línea en el que se encontró el token


    /**
     * Obtiene el tipo del token.
     * @return El tipo del token.
     */
    public Type getType(){
        return type;
    }

    /**
     * Establece el tipo del token.
     * @param type El tipo del token a establecer.
     */
    public void setType(Type type){
        this.type = type;
    }

    /**
     * Obtiene el valor del token.
     * @return El valor del token.
     */
    public String getValue() {
        return value;
    }

    /**
     * Obtiene el número de línea en el que se encontró el token.
     * @param lineNumber número de línea en el que se encontró el token.
     */
    public void setLineNumber(int lineNumber) {this.lineNumber = lineNumber; }

    /**
     * Establece el número de línea en el que se encontró el token.
     * @return  lineNumber El número de línea en el que se encontró el token.
     */
    public int getLineNumber() { return lineNumber; }
    /**
     * Establece el valor del token.
     * @param value El valor del token a establecer.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Establece el lexema del token.
     * @param lexeme El lexema a establecer para este token.
     */
    public void setLexeme(Lexeme lexeme){
        this.lexeme = lexeme;
    }

    /**
     * Obtiene el lexema del token.
     * @return El lexema del token.
     */
    public Lexeme getLexeme(){
        return lexeme;
    }

    /**
     * Enumeración de los diferentes tipos de tokens que pueden ser identificados.
     */
    enum Type{
        NUMBER("\\b[0-9]+\\b"),                 // Representa un número
        MATH_OPERAND("[+\\-*/]"),               // Representa un operador matemático
        COMPARISON_OPERAND("==|<|<=|>|>=|<>"),  // Representa un operador de comparación
        ASSIGNATION_OPERAND("(?<!=)="),         // Representa un operador de asignación
        CONDITIONAL_OPERAND("\\b(if|then|endif|else)\\b"), // Representa un operando condicional
        BOOLEAN_VALUE("\\b(true|false)\\b"),    // Representa un valor booleano
        LOOP("\\b(while|do|endwhile)\\b"),      // Representa un operador de bucle
        PARENTHESIS("\\(|\\)"),
        END(";"),                     // Representa un operador de fin de programa
        VARIABLE("(?!\\b(if|then|endif|else|while|do|endwhile|true|false)\\b)[a-zA-Z_][a-zA-Z0-9_]*"); // Representa una variable

        public final String pattern; // Patrón de expresión regular para identificar el tipo de token

        Type(String s) {
            this.pattern = s;
        }
    }

    /**
     * Enumeración de los diferentes lexemas que pueden ser asociados a los tokens.
     */
    enum Lexeme{
        OPEN_PARENTHESIS ("("),
        CLOSURE_PARENTHESIS(")"),
        WHILE ("while"),
        DO ("do"),
        IF ("if"),
        THEN ("then"),
        ELSE ("else"),
        ENDIF("endif"),
        ENDWHILE("endwhile"),
        ASSIGN("="),
        PLUS("+"),
        MINUS("-"),
        MULTIPLICATION("*"),
        SEMICOLON(";"),
        DIVISION("/"),
        EQUAL_TO("=="),
        LESS_THAN("<"),
        LESS_EQUAL_THAN("<="),
        GREATER_THAN(">"),
        GRATER_EQUAL_THAN(">=");
        public final String lexeme; // Representación de cadena del lexema

        Lexeme(String l) {
            this.lexeme = l;
        }
    }
}