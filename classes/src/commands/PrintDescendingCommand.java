package commands;

import arguments.Argument;
import content.Worker;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class PrintDescendingCommand extends Command<Integer> implements Serializable {
    public PrintDescendingCommand(Argument<Integer> argument) {
        super(argument);
    }

    @Override
    public void execute(LinkedList<Worker> collection) {
        try {
            AtomicReference<String> message = new AtomicReference<>("");
            LinkedList<Worker> printList = new LinkedList<>(collection);
            printList.stream().sorted(Comparator.reverseOrder()).forEach(worker -> message.updateAndGet(v -> v + worker));
            this.setMessage(message.get());
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
    }
}
