public class SyntaxException extends Throwable {
    private final int lineErrorNumber;

    /**
     * Constructor de la clase SyntaxException
     * @param message   Mensaje de error
     * @param lineNumber   Número de línea en la que se encontró el token que genero el error
     */

    public SyntaxException(String message, int lineNumber) {
        super("Syntax error:" + message);
        this.lineErrorNumber = lineNumber;
    }

    public int getLineErrorNumber() {
        return this.lineErrorNumber;
    }
}
