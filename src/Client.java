import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Client <server-port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        // Step 1: Discover the server using UDP
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            udpSocket.setBroadcast(true);

            String discoverMessage = "CCS DISCOVER";
            byte[] buffer = discoverMessage.getBytes();
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, port);

            udpSocket.send(packet);
            System.out.println("Broadcast packet sent: " + discoverMessage);

            byte[] recvBuf = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            udpSocket.receive(receivePacket);

            String responseMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received response: " + responseMessage);

            if (!responseMessage.equals("CCS FOUND")) {
                System.err.println("Unexpected response: " + responseMessage);
                return;
            }

            // Retrieve server's IP and port from the received packet
            InetAddress serverAddress = receivePacket.getAddress();
            int serverPort = receivePacket.getPort();
            System.out.println("Server found at: " + serverAddress.getHostAddress() + ":" + serverPort);

            udpSocket.close();

            // Step 2: Connect to the server using TCP
            try (Socket tcpSocket = new Socket(serverAddress, serverPort);
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(tcpSocket.getOutputStream()), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
                 BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

                System.out.println("Connected to the server at " + serverAddress.getHostAddress() + ":" + serverPort);
                String userInput;

                while (true) {
                    System.out.print("Enter operation (e.g., ADD 5 3 or EXIT to quit): ");
                    userInput = stdIn.readLine();

                    if ("EXIT".equalsIgnoreCase(userInput)) {
                        break;
                    }

                    out.println(userInput);
                    String response = in.readLine();
                    System.out.println("Server response: " + response);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
