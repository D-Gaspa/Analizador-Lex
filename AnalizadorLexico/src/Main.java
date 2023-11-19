import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args){
        String input = "while a < b do\n" +
                "if f == var then\n" +
                "b = c2 + 99\n" +
                "else\n" +
                "b = 37\n" +
                "endif\n" +
                "endwhile";
        System.out.println("Input: " + input);
        ArrayList<Token> tokens = lex(input);
        for (Token token: tokens){
            System.out.println(" Value: " + token.getValue() + " Type: " + token.getType() + " Lexeme: " + token.getLexeme());
        }
    }

    private static ArrayList<Token> lex(String input){
        final ArrayList<Token> tokens = new ArrayList<>(); //Arraylist that will store the tokens found
        final StringTokenizer st = new StringTokenizer(input); //divides phrases and words

        while (st.hasMoreTokens()){
            String word = st.nextToken();
            boolean flag = false;

            for (Token.Type tokenType: Token.Type.values()){

                Pattern pattern = Pattern.compile(tokenType.pattern);
                Matcher search = pattern.matcher(word);
                if (search.find()){
                    Token token = new Token();
                    token.setType(tokenType);
                    token.setValue(word);
                    matchLexeme(word, token);
                    tokens.add(token);
                    flag = true;
                    //System.out.println(" Value: " + token.getValue() + " Type: " + token.getType() + " Lexeme: " + token.getLexeme());
                }
            }
            if (!flag){
                throw new RuntimeException("Invalid token: " + word);

            }
        }
        return tokens;
    }

    private static void matchLexeme(String word, Token token){
        for (Token.Lexeme lexeme : Token.Lexeme.values()) {
            if (word.equals(lexeme.lexeme)) {
                token.setLexeme(lexeme);
                break;
            }
        }
        // If no lexeme is matched, the token's lexeme remains null
    }
    private static boolean isConditionalOperand(String word) {
        return word.matches("\\b(if|then|endif|else|while|do|endwhile)\\b");
    }
    private static boolean isComparisonOperand(String word) {
        return word.matches("==|<|<=|>|>=|<>");
    }

}
