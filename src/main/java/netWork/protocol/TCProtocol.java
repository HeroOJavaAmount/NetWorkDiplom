package netWork.protocol;

/**
 * Централизованное описание протокола обмена.
 * Все константы и вспомогательные методы собраны здесь.
 */
public final class TCProtocol {
    // Протокол Connection
    public static final String REQUEST_LOGIN = "Login:";
    public static final String REQUEST_PASSWORD = "Password:";
    public static final String STATUS_OK = "STATUS OK";
    public static final String STATUS_WRONG = "STATUS WRONG";

    // Команды чата
    public static final String EXIT_COMMAND = "/exit";
    public static final String SERVER_PREFIX = "SERVER:";
    public static final String WELCOME_MESSAGE = SERVER_PREFIX + "Добро пожаловать в чат!";
    public static final String GOODBYE_MESSAGE = SERVER_PREFIX + " Goodbye!";

    // Формат сообщений
    private static final String MESSAGE_FORMAT = "[%s]:%s";

    //  Кодировка и разделители
    public static final String CHARSET_NAME = "UTF-8";
    public static final String LINE_SEPARATOR = "\n";

    /**
     * Форматирует сообщение для рассылки: [имя]:текст
     */
    public static String formatBroadcast(String senderName, String text) {
        return String.format(MESSAGE_FORMAT, senderName, text);
    }

    /**
     * Проверяет, является ли строка командой выхода.
     */
    public static boolean isExitCommand(String line) {
        return line != null && EXIT_COMMAND.equalsIgnoreCase(line.trim());
    }

    /**
     * Проверяет, является ли строка служебным сообщением от сервера.
     */
    public static boolean isServerMessage(String line) {
        return line != null && line.startsWith(SERVER_PREFIX);
    }

    /**
     * Проверяет, является ли строка сообщением от участника (формат [name]:text)
     */
    public static boolean isBroadcastMessage(String line) {
        return line != null && line.startsWith("[") && line.contains("]:");
    }
    /**
     * запрет создания экземпляров
     */
    private TCProtocol() {}
}