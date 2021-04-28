import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            if (args.length == 0)
                throw new Exception("No file as an argument. Please, try to run the program with a filename as an argument.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }

        final int PORT = 4004;

        CollectionManager collectionManager = new CollectionManager(args[0]);
        Server.run(PORT);

        try {
            while (true) {
                try {
                    Server.processRequest(collectionManager);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Connection is aborted! Trying to reconnect...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        System.out.println(ie.getMessage());
                    }
                }
            }
        } finally {
            //ServerPrev.close();
        }
    }
}
