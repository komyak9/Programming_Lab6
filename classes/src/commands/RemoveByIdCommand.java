package commands;

import arguments.Argument;
import content.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class RemoveByIdCommand extends Command<Integer> implements Serializable {
    public RemoveByIdCommand(Argument<Integer> argument) {
        super(argument);
    }

    @Override
    public void execute(LinkedList<Worker> collection) {
        try {
            if (collection.stream().noneMatch(worker -> worker.getSalary() == argument.getArgument()))
                throw new Exception("There is no worker with such salary. Nothing to remove.");
            collection.stream().filter(worker -> worker.getSalary() == argument.getArgument()).map(collection::remove);
            this.setMessage("The elements are removed.");
        } catch (Exception e) {
            this.setMessage(e.getMessage());
        }
    }
}
