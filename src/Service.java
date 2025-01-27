import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Service {
    private Socket clientSocket;
    private ServiceStatistics statistics;

    public Service(Socket clientSocket, ServiceStatistics statistics) {
        this.clientSocket = clientSocket;
        this.statistics = statistics;
    }

    public synchronized void handleClient() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);

                String[] parts = inputLine.split(" ");
                if(parts[0].equals("SPEC_INFO")){
                    out.println(statistics.showSpecificOperation(Integer.parseInt(parts[1])));
                    out.flush();
                    System.out.println("Sent SPEC_INFO");
                    continue;
                } else if (parts.length != 3) {
                    out.println("ERROR");
                    out.flush();
                    ServiceStatistics.numOfNewErrors++;
                    System.out.println("Sent ERROR response");
                    continue;
                }

                try {
                    String operation = parts[0];
                    int num1 = Integer.parseInt(parts[1]);
                    int num2 = Integer.parseInt(parts[2]);

                    String result = statistics.makeOperation(operation, num1, num2);
                    out.println(result);
                    out.flush();
                    System.out.println("Result sent to client: " + result);
                } catch (NumberFormatException e) {
                    out.println("ERROR");
                    out.flush();
                    System.err.println("ERROR: Invalid number format in input: " + inputLine);
                    ServiceStatistics.numOfNewErrors++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error occurred while handling a client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ServiceStatistics.numberOfClients--;
            System.out.println("Client disconnected.");
        }
    }
}
