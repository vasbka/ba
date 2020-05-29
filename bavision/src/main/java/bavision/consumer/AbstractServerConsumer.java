package bavision.consumer;

public class AbstractServerConsumer {
    private static final String protocol = "https://";
    private static final String host = "localhost";
    private static final String port = "3000";

    public static String buildUrl() {
        return protocol + host + port;
    }
}
