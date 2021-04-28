package commands;

import arguments.Argument;
import arguments.IdArgument;
import content.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class UpdateIdCommand extends Command<Integer> implements Serializable {
    public UpdateIdCommand(Argument<Integer> argument) {
        super(argument);
    }

    @Override
    public void execute(LinkedList<Worker> collection) {
        try {
            if (collection.stream().noneMatch(worker -> worker.getId() == argument.getArgument()))
                throw new Exception("There is no worker with such id. Nothing to remove.");

            Worker worker = new Worker(argument.getArgument(), ((IdArgument) argument).getElement().getArgument().getCreationDate(),
                    ((IdArgument) argument).getElement().getArgument().getName(),
                    ((IdArgument) argument).getElement().getArgument().getCoordinates(),
                    ((IdArgument) argument).getElement().getArgument().getSalary(),
                    ((IdArgument) argument).getElement().getArgument().getStartDate(),
                    ((IdArgument) argument).getElement().getArgument().getEndDate(),
                    ((IdArgument) argument).getElement().getArgument().getPosition(),
                    ((IdArgument) argument).getElement().getArgument().getOrganization());
            collection.stream().filter(workerToUpdate -> worker.getId() == argument.getArgument()).map(collection::remove);
            collection.add(worker);
            this.setMessage("The worker is replaced successfully.");
        } catch (Exception ex) {
            this.setMessage(ex.getMessage());
        }
    }
}
