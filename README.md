# Client-server application single-threaded application with xml storage file

Divide the program from lab #5 into client and server modules. The server module must execute the command to manage the collection. The client module must interactively read the data, transfer it to the server for execution, and display the results of the execution.

#### Need to follow the requirements:

1. Operations for processing collections of objects must be implemented using the Stream API using lambda expressions.
2. Objects between client and server must be transferred in serialized form.
3. Objects in a collection accepted by the client must be sorted by name.
4. The client must correctly manage the temporary unavailability of the server.
5. Data exchange between clients and the server must transfer over TCP.
6. To transfer data to the server, you must use the network channel.
7. To transfer data on the client, you must use I/O streams.
8. Network channels must be in a non-blocking protocol.

#### Responsibilities of the server application:

1. Working with a file that stores a collection.
2. Managing a collection of objects.
3. Assignment of automatically generated fields of objects in the collection.
4. Waiting for connections and requests from the client.
5. Processing of received requests (commands).
6. Saving the collection to a file when the application terminates.
7. Saving the collection to a file when executing a special command available only to the server (the client cannot send such a command).

The server application should consist of the following modules (implemented as one or more classes):
1. Connection module.
2. Request Reader.
3. Module for processing received commands.
4. Module for sending responses to the client.

The server must be running in single-threaded mode.

#### Responsibilities of the client application:

1. Reading commands from the console.
2. Validation of input data.
3. Serialization of the entered command and its arguments.
4. Sending the received command and its arguments to the server.
5. Processing the response from the server (outputting the result of the command execution to the console).
6. The save command from the client application must be removed.
7. The exit command terminates the client application.

Important! Commands and their arguments must be class objects. The exchange of "plain" strings is not allowed. So, for the add command or its equivalent, you need to form an object containing the command type and the object that should be stored in your collection.

Additional task (DONE):
Implement logging of various stages of server operation (starting work, getting a new connection, receiving a new request, sending a response, etc.) using Logback.

