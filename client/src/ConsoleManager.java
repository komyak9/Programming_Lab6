import commands.Command;
import commands.ExitCommand;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleManager {
    private final Scanner scanner = new Scanner(System.in);
    private final CommandValidator commandValidator = new CommandValidator();

    public Command<?> readCommand() {
        Command<?> command = null;
        try {
            do {
                System.out.println("\nPLease, enter a command. You can inspect the list of available commands by \"help\".");
                try {
                    if (!scanner.hasNextLine())
                        throw new Exception("There is no line. Program's going to be finished.");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    System.exit(0);
                }
            } while ((command = commandValidator.validateData(scanner.nextLine(), scanner)) == null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return command;
    }

    public void communicate(String host, int port) throws IOException {
        Command<?> command;
        while (!Client.getSocket().isOutputShutdown()) {
            try {
                command = this.readCommand();
                if (command.getClass() == ExitCommand.class)
                    break;
                Client.sendData(command);
                System.out.println("Server's response:" + Client.receiveData());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //System.out.println("Connection is aborted! Trying to reconnect...");
                Client.getOut().flush();
                Client.connect(host, port);
            }
        }
    }
}
