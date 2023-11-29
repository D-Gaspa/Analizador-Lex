import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    /**
     * Ejecuta los ejemplos de entrada y muestra los resultados.
     * @param fileName Nombre del archivo de entrada.
     */
    public static void runExamples(String fileName) {
        String input = "";

        // Read file
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator()); // Preserve newlines
            }
            input = stringBuilder.toString();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        // Print the file name
        System.out.println("\n ------------------------------------------ " + fileName + " --------------------------------------------\n");

        // Print the input
        System.out.println("Input: " + input);

        // Run the lexical analysis and syntax analysis
        ArrayList<Token> tokens;

        final ArrayList<SyntaxException> errors = new ArrayList<>();

        // Lexical analysis
        try {
            tokens = lex(input,errors);
            System.out.println(" -------------------------------------  Tokens:  -----------------------------------------");
            for (Token token : tokens){
                System.out.println(" Value: " + token.getValue() + " Type: " + token.getType() + " Lexeme: " + token.getLexeme());
            }
            // Syntax analysis
            System.out.println("\n ------------------------------------------ Syntax analysis:  --------------------------------------------\n");

            // Parser initialization and syntax checking
            Parser parser = new Parser(tokens, errors);
            try {
                parser.parse(); // Parsing the token list
            } catch (SyntaxException e) {
                errors.add(e);
            }
        } catch (SyntaxException e) {
            errors.add(e);
        }

        errors.sort(Comparator.comparingInt(SyntaxException::getLineErrorNumber));
        for (SyntaxException error : errors) {
            System.out.println(error.getMessage());
        }
    }

    /**
     * Método principal para iniciar el análisis léxico.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args)  {
        runExamples("input1.txt");
        runExamples("input2.txt");
        runExamples("input3.txt");
    }

    /**
     * Realiza el análisis léxico de la cadena de entrada y devuelve una lista de tokens.
     * @param input Cadena de entrada para analizar.
     * @return ArrayList de tokens encontrados en la entrada.
     */
    private static ArrayList<Token> lex(String input, ArrayList<SyntaxException> errors) throws SyntaxException {
        final ArrayList<Token> tokens = new ArrayList<>();
        String[] lines = input.split("\\r?\\n"); // Separa la entrada en líneas
        int lineNumber = 0;

        for (String line : lines) {
            lineNumber++; // Incrementa el número de línea de cada token
            StringTokenizer st = new StringTokenizer(line, " \t", true); // Tokenize the line

            while (st.hasMoreTokens()) {
                String word = st.nextToken();

                if (word.trim().isEmpty()) {
                    continue; // Salta espacios
                }


                boolean matched = false;

                // Procesamiento de todos los tokens menos VARIABLE
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

                // Checar por lexemas de tipo variable
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
                        // Si no es token válido se arroja un error
                        errors.add(new SyntaxException("Token " + word +" not recognized " + " at line " + lineNumber , lineNumber ));
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
}