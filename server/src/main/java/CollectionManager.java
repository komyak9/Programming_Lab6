import commands.Command;
import content.Worker;
import creation.IdGenerator;
import parser.WorkerParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class CollectionManager {
    private final IdGenerator idGenerator = new IdGenerator();
    private LinkedList<Worker> workersList;
    private File file;

    public CollectionManager(String fileName) {
        try {
            file = new File(fileName);
            if (!file.exists())
                throw new FileNotFoundException();
        } catch (Exception ex) {
            App.logger.error(ex.getMessage());
        }
        fillCollection();
    }

    public String execute(Command<?> command) {
        command.setIdGenerator(idGenerator);
        command.execute(workersList);
        return command.getMessage();
    }

    private void fillCollection() {
        try {
            WorkerParser workerParser = new WorkerParser();
            workersList = workerParser.getWorkersList(file);
            for (Worker worker : workersList)
                idGenerator.addId(worker.getId());

            System.out.println("Data from the file were successfully downloaded.");
        } catch (Exception ex) {
            App.logger.error(ex.getMessage());
            App.logger.error("Error in xml file data. Please, check the data in the file.");
        }
    }

    public LinkedList<Worker> getWorkersList() {
        return workersList;
    }
}
