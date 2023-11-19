public class Token {
    private String value;
    private Type type;
    private Lexeme lexeme;

    public Type getType(){
        return type;
    }
    public void setType(Type type){
        this.type = type;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public void setLexeme(Lexeme lexeme){
        this.lexeme = lexeme;
    }
    public Lexeme getLexeme(){
        return lexeme;
    }

    enum Type{
        NUMBER("\\b[0-9]+\\b"),
        MATH_OPERAND("[+\\-*/]"),
        COMPARISON_OPERAND("==|<|<=|>|>=|<>"),
        ASSIGNATION_OPERAND("(?<!=)="),
        CONDITIONAL_OPERAND("\\b(if|then|endif|else)\\b"),
        BOOLEAN_VALUE("\\b(true|false)\\b"),
        LOOP("\\b(while|do|endwhile)\\b"),
        VARIABLE("(?!\\b(if|then|endif|else|while|do|endwhile|true|false)\\b)[a-zA-Z_][a-zA-Z0-9_]*");
        //PROGRAM NOT RECOGNIZING BETWEEN A VARIABLE AND A CONDITIONAL OPERAND
        public final String pattern;

        Type(String s) {
            this.pattern = s;
        }
    }

    enum Lexeme{
        WHILE ("while"),
        DO ("do"),
        IF ("if"),
        THEN ("then"),
        ELSE ("else"),
        ENDIF("endif"),
        ENDWHILE("endwhile"),
        ASIGN("="),
        PLUS("+"),
        MINUS("-"),
        MULTIPLICATION("*"),
        DIVISION("/"),
        EQUAL_TO("=="),
        LESS_THAN("<"),
        LESS_EQUAL_THAN("<="),
        GREATER_THAN(">"),
        GRATER_EQUAL_THAN(">=");
        public final String lexeme;

        Lexeme(String l) {
            this.lexeme = l;
        }
    }

}
