import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    /**
     * Método principal para iniciar el análisis léxico.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args){
        // Entrada de ejemplo para el análisis léxico
        String input = "while a < b do\n" +
                "if f == var then\n" +
                "b = c2 + 99\n" +
                "else\n" +
                "b = 37\n" +
                "endif\n" +
                "endwhile";
        System.out.println("Input: " + input);

        // Proceso de análisis léxico y obtención de tokens
        ArrayList<Token> tokens = lex(input);
        for (Token token : tokens){
            System.out.println(" Value: " + token.getValue() + " Type: " + token.getType() + " Lexeme: " + token.getLexeme());
        }

        // Parser initialization and syntax checking
        Parser parser = new Parser(tokens);
        try {
            parser.parse(); // Parsing the token list
            System.out.println("Parsing completed successfully.");
        } catch (Parser.ParseException e) {
            System.out.println("Parsing error: " + e.getMessage());
        }
    }

    /**
     * Realiza el análisis léxico de la cadena de entrada y devuelve una lista de tokens.
     * @param input Cadena de entrada para analizar.
     * @return ArrayList de tokens encontrados en la entrada.
     */
    private static ArrayList<Token> lex(String input) {
        final ArrayList<Token> tokens = new ArrayList<>();
        final StringTokenizer st = new StringTokenizer(input);

        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            boolean flag = false;

            // Procesamiento de cada tipo de token excepto VARIABLE
            for (Token.Type tokenType : Token.Type.values()) {
                if (tokenType == Token.Type.VARIABLE) {
                    continue;
                }

                Pattern pattern = Pattern.compile(tokenType.pattern);
                Matcher search = pattern.matcher(word);
                if (search.find()) {
                    Token token = new Token();
                    token.setType(tokenType);
                    token.setValue(word);
                    matchLexeme(word, token);
                    tokens.add(token);
                    flag = true;
                    break;
                }
            }

            // Verificación final para el tipo VARIABLE
            if (!flag) {
                Pattern pattern = Pattern.compile(Token.Type.VARIABLE.pattern);
                Matcher search = pattern.matcher(word);
                if (search.find()) {
                    Token token = new Token();
                    token.setType(Token.Type.VARIABLE);
                    token.setValue(word);
                    tokens.add(token);
                } else {
                    // Si no se encuentra ningún tipo de token válido, se lanza una excepción
                    throw new RuntimeException("Invalid token: " + word);
                }
            }
        }
        return tokens;
    }

    /**
     * Asigna un lexema al token basado en su valor.
     * @param word La palabra actual para la que se está creando el token.
     * @param token El token al que se le asignará el lexema.
     */
    private static void matchLexeme(String word, Token token){
        for (Token.Lexeme lexeme : Token.Lexeme.values()) {
            if (word.equals(lexeme.lexeme)) {
                token.setLexeme(lexeme);
                break;
            }
        }
    }

    // Los siguientes métodos podrían ser útiles para futuras expansiones o verificaciones adicionales
    private static boolean isConditionalOperand(String word) {
        return word.matches("\\b(if|then|endif|else|while|do|endwhile)\\b");
    }
    private static boolean isComparisonOperand(String word) {
        return word.matches("==|<|<=|>|>=|<>");
    }
}
