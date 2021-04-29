import content.Worker;
import parser.WorkerParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Save {
    public void save(String fileName, CollectionManager collectionManager) throws IOException, IllegalAccessException {
        File file = new File(fileName);
        FileOutputStream fstream = new FileOutputStream(file, true);
        String line = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<workers>\n";
        byte[] buffer = line.getBytes();
        fstream.write(buffer);

        WorkerParser parser = new WorkerParser();
        for (Worker worker : collectionManager.getWorkersList()) {
            parser.parseObjectToXML(file, worker);
        }
    }
}
