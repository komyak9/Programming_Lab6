import commands.Command;
import content.Worker;
import creation.IdGenerator;
import parser.WorkerParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public class Save {
        public void save() {
            try {
                FileOutputStream fstream = new FileOutputStream(file, true);
                String line = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<workers>\n";
                byte[] buffer = line.getBytes();
                fstream.write(buffer);

                WorkerParser parser = new WorkerParser();
                for (Worker worker : workersList) {
                    parser.parseObjectToXML(file, worker);
                }

                line = "</workers>\n";
                buffer = line.getBytes();
                fstream.write(buffer);
            } catch (Exception e) {
                App.logger.error(e.getMessage());
            }
        }
    }

    public LinkedList<Worker> getWorkersList() {
        return workersList;
    }
}
