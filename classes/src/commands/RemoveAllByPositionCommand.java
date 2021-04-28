package commands;

import arguments.Argument;
import content.Position;
import content.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class RemoveAllByPositionCommand extends Command<Position> implements Serializable {
    public RemoveAllByPositionCommand(Argument<Position> argument) {
        super(argument);
    }

    @Override
    public void execute(LinkedList<Worker> collection) {
        try {
            if (collection.stream().noneMatch(worker -> worker.getPosition().equals(argument.getArgument())))
                throw new Exception("There is no worker with such position. Nothing to remove.");
            collection.stream().filter(worker -> worker.getPosition().equals(argument.getArgument())).map(collection::remove);
            this.setMessage("The elements are removed.");
        } catch (Exception e) {
            this.setMessage(e.getMessage());
        }
    }
}
