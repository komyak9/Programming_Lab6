import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App {
    public static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args){
        try {
            if (args.length == 0)
                throw new Exception("No file as an argument. Please, try to run the program with a filename as an argument.");

            final int PORT = 3175;
            Server.run(PORT);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.exit(0);
        }

        CollectionManager collectionManager = new CollectionManager(args[0]);
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
            try {
                new Save().save(collectionManager);
            }catch (IllegalAccessException | IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
