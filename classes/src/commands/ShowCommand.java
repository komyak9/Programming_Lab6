package commands;

import arguments.Argument;
import content.Worker;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class ShowCommand extends Command<Integer> implements Serializable {
    public ShowCommand(Argument<Integer> argument) {
        super(argument);
    }

    @Override
    public void execute(LinkedList<Worker> collection) {
        try {
            if (collection.isEmpty())
                throw new Exception("Collection is empty. Nothing to show.");
            else {
                AtomicReference<String> message = new AtomicReference<>("");
                collection.forEach(worker -> message.updateAndGet(v -> v + worker));
                this.setMessage(message.get());
            }
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
    }

}
