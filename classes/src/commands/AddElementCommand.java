package commands;

import arguments.Argument;
import content.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class AddElementCommand extends Command<Worker> implements Serializable {
    public AddElementCommand(Argument<Worker> argument) {
        super(argument);
    }

    @Override
    public void execute(LinkedList<Worker> collection) {
        try {
            Worker worker = new Worker(idGenerator.generateId(), argument.getArgument().getCreationDate(),
                    argument.getArgument().getName(), argument.getArgument().getCoordinates(),
                    argument.getArgument().getSalary(), argument.getArgument().getStartDate(),
                    argument.getArgument().getEndDate(), argument.getArgument().getPosition(),
                    argument.getArgument().getOrganization());
            collection.add(worker);
            this.setMessage("New worker was successfully added to the collection.");
        } catch (Exception ex) {
            this.setMessage(ex.getMessage() + "\n New element wasn't added.");
        }
    }
}
