public class SyntaxException extends Throwable {
    private int lineErrorNumber;
    public SyntaxException(String message, int lineNumber) {
        super("Syntax error:" + message);
        this.lineErrorNumber = lineNumber;
    }

    public int getLineErrorNumber() {
        return this.lineErrorNumber;
    }
}
