import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceStatistics {
    final LocalDateTime startTime;
    private List<String> operations;
    private List<Integer> number1;
    private List<Integer> number2;
    private List<Integer> results;

    public static int numberOfClients = 0;
    public static int newClients = 0;
    public static int numberOfOperations = 0;
    public static int numOfNewOperations = 0;
    public static int numberOfADD = 0;
    public static int numOfNewADD = 0;
    public static int numberOfSUB = 0;
    public static int numOfNewSUB = 0;
    public static int numberOfMUL = 0;
    public static int numOfNewMUL = 0;
    public static int numberOfDIV = 0;
    public static int numOfNewDIV = 0;
    public static int numberOfErrors = 0;
    public static int numOfNewErrors = 0;
    public static int sumOfAllOperations = 0;
    public static int sumOfAllNewOperations = 0;

    public ServiceStatistics() {
        this.startTime = LocalDateTime.now();
        this.operations = new ArrayList<>();
        this.number1 = new ArrayList<>();
        this.number2 = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public String makeOperation(String operation, int num1, int num2){
        int result;
        switch (operation) {
            case "ADD":
                result = num1 + num2;
                numOfNewADD++;
                break;
            case "SUB":
                result = num1 - num2;
                numOfNewSUB++;
                break;
            case "MUL":
                result = num1 * num2;
                numOfNewMUL++;
                break;
            case "DIV":
                if (num2 == 0) {
                    numOfNewErrors++;
                    System.err.println("Division by zero!");
                    return "ERROR";
                }
                result = num1 / num2;
                numOfNewDIV++;
                break;
            default:
                numOfNewErrors++;
                System.err.println("Wrong operation or typo!");
                return "ERROR";
        }

        updateStatistics(operation, num1, num2, result);
        return String.valueOf(result);
    }

    private void updateStatistics(String operation, int num1, int num2, int result) {
        operations.add(operation);
        number1.add(num1);
        number2.add(num2);
        results.add(result);
        numOfNewOperations++;
        sumOfAllNewOperations += result;
    }

    public void runStatistics(){
        while(true){
            System.out.println("*** Data since start (" + startTime + ") ***");
            System.out.println("Number of all clients: " + (numberOfClients + newClients));
            System.out.println("Number of operations: " + (numberOfOperations + numOfNewOperations));
            System.out.println("Number of ADD operations: " + (numberOfADD + numOfNewADD));
            System.out.println("Number of SUB operations: " + (numberOfSUB + numOfNewSUB));
            System.out.println("Number of MUL operations: " + (numberOfMUL + numOfNewMUL));
            System.out.println("Number of DIV operations: " + (numberOfDIV + numOfNewDIV));
            System.out.println("Sum of all operations: " + (sumOfAllOperations + sumOfAllNewOperations));
            System.out.println("Number of Errors: " + (numberOfErrors + numOfNewErrors));
            System.out.println();

            System.out.println("--- Data from last 10 seconds ---");
            System.out.println("Number of new clients: " + newClients);
            System.out.println("Number of new operations: " + numOfNewOperations);
            System.out.println("Number of new ADD operations: " + numOfNewADD);
            System.out.println("Number of new SUB operations: " + numOfNewSUB);
            System.out.println("Number of new MUL operations: " + numOfNewMUL);
            System.out.println("Number of new DIV operations: " + numOfNewDIV);
            System.out.println("Sum of all new operations: " + sumOfAllNewOperations);
            System.out.println("Number of new Errors: " + numOfNewErrors);
            System.out.println();

            numberOfClients += newClients;
            newClients = 0;
            numberOfOperations += numOfNewOperations;
            numOfNewOperations = 0;
            numberOfADD += numOfNewADD;
            numOfNewADD = 0;
            numberOfSUB += numOfNewSUB;
            numOfNewSUB = 0;
            numberOfMUL += numOfNewMUL;
            numOfNewMUL = 0;
            numberOfDIV += numOfNewDIV;
            numOfNewDIV = 0;
            sumOfAllOperations += sumOfAllNewOperations;
            sumOfAllNewOperations = 0;
            numberOfErrors += numOfNewErrors;
            numOfNewErrors = 0;

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.err.println("Statistics reporting interrupted");
                throw new RuntimeException(e);
            }
        }
    }

    public String showSpecificOperation(int id){
        if(id>=0 && id<operations.size()) {
            return "Operation id: " + id + " -> " + operations.get(id) + " "
                    + number1.get(id) + " " + number2.get(id) + ", result: " + results.get(id);
        } else {
            return "Error occurred while checking specific operation";
        }
    }

}
