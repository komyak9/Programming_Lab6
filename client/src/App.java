import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 3175;
        Client.connect(host, port);

        ConsoleManager consoleManager = new ConsoleManager();
        try {
            consoleManager.communicate(host, port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Connection is aborted! Trying to reconnect...");
            try {
                Client.getOut().flush();
                Client.connect(host, port);
            } catch (Exception ex) {
                System.out.println(e.getMessage());
            }
        } finally {
            try {
                Client.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
