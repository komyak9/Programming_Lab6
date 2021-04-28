package commands;

import arguments.Argument;
import content.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class RemoveGreaterCommand extends Command<Worker> implements Serializable {
    public RemoveGreaterCommand(Argument<Worker> argument) {
        super(argument);
    }

    @Override
    public void execute(LinkedList<Worker> collection) {
        try {
            if (collection.stream().noneMatch(worker -> argument.getArgument().compareTo(worker) < 0))
                throw new Exception("There is no workers greater. Nothing to remove.");
            collection.stream().filter(worker -> argument.getArgument().compareTo(worker) < 0).map(collection::remove);
            this.setMessage("The elements are removed.");
        } catch (Exception e) {
            this.setMessage(e.getMessage());
        }
    }
}
