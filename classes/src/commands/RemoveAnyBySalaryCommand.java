package commands;

import arguments.Argument;
import content.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class RemoveAnyBySalaryCommand extends Command<Float> implements Serializable {
    public RemoveAnyBySalaryCommand(Argument<Float> argument) {
        super(argument);
    }

    @Override
    public void execute(LinkedList<Worker> collection) {

    }
}
