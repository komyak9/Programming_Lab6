package commands;

import arguments.Argument;
import content.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class ExitCommand extends Command<Integer> implements Serializable {
    public ExitCommand(Argument<Integer> argument) {
        super(argument);
    }

    @Override
    public void execute(LinkedList<Worker> collection) {
        System.out.println("Client is finishing...");
    }
}
