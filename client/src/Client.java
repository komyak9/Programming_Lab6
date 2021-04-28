import commands.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static String host;
    public static int port;
    private static Socket socket;
    private static OutputStream out;
    private static InputStream in;

    public static void connect(String hostage, int socketPort) {
        host = hostage;
        port = socketPort;
        boolean scanning = true;
        while (scanning) {
            try {
                socket = new Socket(host, port);
                scanning = false;
                System.out.println("Connection with the server is established!");
                out = socket.getOutputStream();
                in = socket.getInputStream();
            } catch (Exception e) {
                System.out.println("Connection with the server is failed, waiting and trying again...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    System.out.println(ie.getMessage());
                }
            }
        }
    }

    public static void sendData(Command<?> command) throws IOException {
        if (!socket.isOutputShutdown())
            out.write(new Serializer().serialize(command));
        out.flush();
        System.out.println("Data to the server was successfully sent.");
    }

    public static String receiveData() throws IOException, ClassNotFoundException {
        byte[] bytes = new byte[1000000];
        in.read(bytes);
        return new String(bytes).replaceAll("\u0000.*", "");
    }

    public static void close() throws IOException {
        System.out.println("Closing the client...");
        in.close();
        out.close();
        socket.close();
    }

    public static Socket getSocket() {
        return socket;
    }

    public static OutputStream getOut() {
        return out;
    }
}