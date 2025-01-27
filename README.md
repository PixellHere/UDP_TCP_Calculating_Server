# UDP/TCP Calculating server

**Description**

The CCS Server Application is a multithreaded server implemented in Java that supports both UDP and TCP protocols. 
It allows clients to perform mathematical operations and monitor server statistics. 
The server is capable of handling multiple clients simultaneously and responds to specific protocol messages.

**Features**

UDP Protocol:
- Listens for "CCS DISCOVER" messages and responds with "CCS FOUND".
- Enables clients to discover the server within the local network.
TCP Protocol:
- Accepts and processes mathematical operations sent by clients in the format <OPERATION> <NUMBER1> <NUMBER2>.

Supported operations:
- ADD (addition)
- SUB (subtraction)
- MUL (multiplication)
- DIV (division)
Returns results as integers or an ERROR message for invalid inputs.

Server Statistics:
- Tracks and displays:
- Total number of clients.
- Total number of operations performed.
- Breakdown of operations by type.
- Total errors encountered.
- Displays statistics every 10 seconds in the server console.

**Requirements**

Java Development Kit (JDK): Version 1.8 or higher.

**Installation & Setup**

Compile the source files:
javac CSS.java Service.java ServiceStatistics.java

Run the server:
java -jar CSS.jar <port>
Replace <port> with the desired port number for the server.

Client Interaction:
UDP: Send a broadcast message with "CCS DISCOVER" to discover the server.
TCP: Connect to the server using the same port and send operations in the specified format.
Usage

Example TCP Commands:
- ADD 5 3 → Returns 8.
- DIV 10 0 → Returns ERROR (division by zero).
- SPEC_INFO <ID> → Returns details of a specific operation by its ID.

UDP Protocol:
- Sends "CCS DISCOVER". 
- Receives "CCS FOUND".

**Known Issues:**

- Extensive testing for scalability with high concurrency has not been performed.
- Server assumes clients always send valid UTF-8 encoded strings.

**Author**

[Kacper Płachetka]

[Discord: pixelhere]