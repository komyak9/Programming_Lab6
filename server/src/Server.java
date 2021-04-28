import commands.Command;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static Selector selector;
    private static SocketAddress socketAddress;
    private static ServerSocketChannel serverSocketChannel;

    public static void run(int port) throws IOException, ClassNotFoundException {
        socketAddress = new InetSocketAddress(port);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(socketAddress);
        serverSocketChannel.configureBlocking(false);
        //selector = Selector.open();
        //serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static void processRequest(CollectionManager collectionManager) throws IOException, ClassNotFoundException {
        Command<?> command;
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while (true) {
            System.out.println("Waiting for select...");
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();

            for (Iterator iter = selectedKeys.iterator(); iter.hasNext(); ) {
                SelectionKey key = (SelectionKey) iter.next();

                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Connection accepted.");
                    }
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(1000000);
                        socketChannel.read(buffer);
                        buffer.flip();
                        command = (Command<?>) (new Serializer().deserialize(buffer.array()));


                        String result = collectionManager.execute(command);
                        System.out.println("A string to send: " + result);
                        socketChannel.write(ByteBuffer.wrap(result.getBytes()));
                    }
                }
                iter.remove();
            }
        }
    }
}
