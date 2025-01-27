import java.io.IOException;
import java.net.*;

public class CSS {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Expected: java -jar CSS.jar <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        ServiceStatistics statistics = new ServiceStatistics();

        Thread statisticsThread = new Thread(statistics::runStatistics);
        statisticsThread.start();

        try {
            System.out.println("Attempting to bind to port " + port);
            DatagramSocket udpSocket = new DatagramSocket(port);
            new Thread(() -> startUDP(udpSocket)).start();

            ServerSocket tcpServerSocket = new ServerSocket(port);
            startTCP(tcpServerSocket, statistics);

        } catch (BindException e) {
            System.err.println("Port " + port + " is already in use. Please choose a different port.");
            statisticsThread.interrupt();
        } catch (IOException e) {
            System.out.println("Port occupied, open in different port");
            e.printStackTrace();
        }
    }

    public static void startUDP(DatagramSocket socket) {
        try {
            while (true) {
                System.out.println("Waiting for UDP data");
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received: " + receivedMessage);

                if (receivedMessage.startsWith("CCS DISCOVER")) {
                    String responseMessage = "CCS FOUND";
                    byte[] responseBytes = responseMessage.getBytes();

                    InetAddress clientAddress = packet.getAddress();
                    int clientPort = packet.getPort();

                    DatagramPacket responsePacket = new DatagramPacket(
                            responseBytes, responseBytes.length, clientAddress, clientPort
                    );

                    socket.send(responsePacket);
                    System.out.println("Sent response to " + clientAddress + ":" + clientPort);
                }
            }
        } catch (IOException e) {
            System.err.println("Error in UDP server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Socket closed.");
            }
        }
    }

    public static void startTCP(ServerSocket serverSocket, ServiceStatistics statistics) {
        try {
            while (true) {
                System.out.println("Waiting for TCP connections");
                Socket clientSocket = serverSocket.accept();
                System.out.println("New TCP client connected");

                ServiceStatistics.newClients++;

                new Thread(() -> new Service(clientSocket, statistics).handleClient()).start();
            }
        } catch (IOException e) {
            System.err.println("Error in TCP server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
