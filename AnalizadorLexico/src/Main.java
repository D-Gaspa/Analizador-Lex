import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    /**
     * Método principal para iniciar el análisis léxico.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) throws SyntaxException {
        final ArrayList<SyntaxException> errors = new ArrayList<>();

        // Entrada de ejemplo para el análisis léxico
        String input = "a = b ;\n" +
                "if a < b  then \n" +
                "a = a + 1 ; \n" +
                "b = 2+7) * 3- ;  \n" +
                "endif \n";

        String input1 = "while a < b do \n" +
                "if f == var then \n" +
                "b = c2 + 99 ; \n" +
                "else \n" +
                "b = 37 ; \n" +
                "endif \n" +
                "endwhile";
        System.out.println("Input: " + input);

        // Proceso de análisis léxico y obtención de tokens

        ArrayList<Token> tokens;

        try {
            tokens = lex(input,errors);
            for (Token token : tokens){
                //System.out.println(" Value: " + token.getValue() + " Type: " + token.getType() + " Lexeme: " + token.getLexeme());
            }

            // Parser initialization and syntax checking
            Parser parser = new Parser(tokens, errors);
            try {
                parser.parse(); // Parsing the token list
            } catch (SyntaxException e) {
                errors.add(e);
                //System.out.println("Error in parsing: " + e.getMessage());
            }
        } catch (SyntaxException e) {
            errors.add(e);
            //System.out.println("Error in tokenization: " + e.getMessage());
        }


        for (SyntaxException error : errors) {
            System.out.println(error.getMessage());
        }
    }

    /**
     * Realiza el análisis léxico de la cadena de entrada y devuelve una lista de tokens.
     * @param input Cadena de entrada para analizar.
     * @return ArrayList de tokens encontrados en la entrada.
     */
    private static ArrayList<Token> lex(String input, ArrayList<SyntaxException> errors) throws SyntaxException {
        final ArrayList<Token> tokens = new ArrayList<>();
        String[] lines = input.split("\\r?\\n"); // Split the input into lines
        int lineNumber = 0;

        for (String line : lines) {
            lineNumber++; // Increment the line number for each new line
            StringTokenizer st = new StringTokenizer(line, " \t", true); // Tokenize the line

            while (st.hasMoreTokens()) {
                String word = st.nextToken();

                if (word.trim().isEmpty()) {
                    continue; // Skip whitespace
                }


                boolean matched = false;

                // Process each type of token except VARIABLE
                for (Token.Type tokenType : Token.Type.values()) {
                    if (tokenType == Token.Type.VARIABLE) {
                        continue;
                    }

                    Pattern pattern = Pattern.compile(tokenType.pattern);
                    Matcher matcher = pattern.matcher(word);
                    if (matcher.matches()) {
                        Token token = new Token();
                        token.setType(tokenType);
                        token.setValue(word);
                        token.setLineNumber(lineNumber);
                        matchLexeme(word, token);
                        tokens.add(token);
                        matched = true;
                        break;
                    }
                }

                // Final check for the VARIABLE type
                if (!matched) {
                    Pattern variablePattern = Pattern.compile(Token.Type.VARIABLE.pattern);
                    Matcher variableMatcher = variablePattern.matcher(word);
                    if (variableMatcher.matches()) {
                        Token token = new Token();
                        token.setType(Token.Type.VARIABLE);
                        token.setValue(word);
                        token.setLineNumber(lineNumber);
                        tokens.add(token);
                    } else {
                        // If no valid type of token is found, throw an exception
                        errors.add(new SyntaxException("Syntax error: " + word + " at line " + lineNumber + " (token not recognized)"));
                    }
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
