import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;

public class App {
    public static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            if (args.length == 0)
                throw new Exception("No file as an argument. Please, try to run the program with a filename as an argument.");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.exit(0);
        }

        final int PORT = 3175;
        CollectionManager collectionManager = new CollectionManager(args[0]);
        Server.run(PORT);

        boolean toContinue = true;
        try {
            while (toContinue) {
                long start = System.currentTimeMillis();
                try {
                    toContinue = Server.processRequest(collectionManager, start);
                } catch (IOException | ClassNotFoundException e) {
                    logger.warn("Connection is aborted! Trying to reconnect...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        logger.error(ie.getMessage());
                    }
                }
            }
        } finally {

        }
    }
}
