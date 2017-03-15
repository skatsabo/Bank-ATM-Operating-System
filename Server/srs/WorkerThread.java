
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerThread implements Runnable {  

    private final String[] data;
    private final Socket departmentSocket;
    private final ArrayList<Record> records;

    private DatabaseHandler dh;

    public WorkerThread(String[] data, Socket departmentSocket, ArrayList<Record> records) {
        this.data = data;
        this.departmentSocket = departmentSocket;
        this.records = records;
    }

    @Override
    public void run() { 

        dh = new Main().getDatabaseHandler();  //για το update

        System.out.println("Server << " + Arrays.toString(data));

        if (data[1].equals("Q1")) {
            try {
                records.get(findIndexFromAccountNumber(data[2])).availableAcquire();
                records.get(findIndexFromAccountNumber(data[2])).availableRelease();

                executeQueryType1(data);

            } catch (IOException ex) {
                Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (data[1].equals("Q2")) {
            try {
                records.get(findIndexFromAccountNumber(data[2])).availableAcquire();

                executeQueryType2(data);

                records.get(findIndexFromAccountNumber(data[2])).availableRelease();

            } catch (IOException ex) {
                Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (data[1].equals("Q3")) {
            try {
                records.get(findIndexFromAccountNumber(data[2])).availableAcquire();
                records.get(findIndexFromAccountNumber(data[3])).availableAcquire();

                executeQueryType3(data);

                records.get(findIndexFromAccountNumber(data[2])).availableRelease();
                records.get(findIndexFromAccountNumber(data[3])).availableRelease();

            } catch (IOException ex) {
                Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (data[1].equals("Q4")) {
            try {
                for (int i = 0; i < records.size(); i++) {
                    if (records.get(i).getDepartmentNumber() == data[0].charAt(0)) {
                        records.get(i).availableAcquire();
                    }
                }

                executeQueryType4(data); 

            } catch (IOException ex) {
                Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void sendToDepartment(String message, PrintWriter dWriter) {
        PrintWriter writer = dWriter;

        writer.println(message);
        writer.flush();
    }

    private void executeQueryType1(String[] data) throws IOException {

        float amount = findAmountFromAccountNumber(data[2]);

        // Send to client
        sendToDepartment("Query : " + Arrays.toString(data), new PrintWriter(departmentSocket.getOutputStream()));
        sendToDepartment("Amount : " + amount, new PrintWriter(departmentSocket.getOutputStream()));
    }

    private void executeQueryType2(String[] data) throws IOException {

        int ans = updateAccountBalance(data[2], data[3], Float.parseFloat(data[4]));

        sendToDepartment("Query : " + Arrays.toString(data), new PrintWriter(departmentSocket.getOutputStream()));

        if (ans == 1) {
            sendToDepartment("Action completed...", new PrintWriter(departmentSocket.getOutputStream()));
            dh.updateFile(records);
        } else {
            sendToDepartment("Insufficient balance 2", new PrintWriter(departmentSocket.getOutputStream()));
        }
    }

    private void executeQueryType3(String[] data) throws IOException {

        int ans = transferMoney(data[2], data[3], Float.parseFloat(data[4]));

        sendToDepartment("Query : " + Arrays.toString(data), new PrintWriter(departmentSocket.getOutputStream()));

        if (ans == 1) {
            sendToDepartment("Transfer completed...", new PrintWriter(departmentSocket.getOutputStream()));
            dh.updateFile(records);
        } else {
            sendToDepartment("Insufficient balance 3", new PrintWriter(departmentSocket.getOutputStream()));
        }
    }

    private void executeQueryType4(String[] data) throws IOException {

        float sum = 0;
        int count = 0;

        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getDepartmentNumber() == data[0].charAt(0)) {
                sum += records.get(i).getAmount();
                count++;

                records.get(i).availableRelease();
            }
        }
        float avg = sum / count;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a");
        String fDate = sdf.format(date);

        sendToDepartment("Query : " + Arrays.toString(data), new PrintWriter(departmentSocket.getOutputStream()));

        sendToDepartment("Average from Department " + data[0] + " is : " + avg + " Time Stamp : " + fDate, new PrintWriter(departmentSocket.getOutputStream()));
    }

    private int findIndexFromAccountNumber(String accountNumber) { 
        int i = 0;
        int index = -1;

        while (i < records.size() && index == -1) {
            if (records.get(i).getAccountNumber() == Integer.parseInt(accountNumber)) {
                index = i;
            }
            i++;
        }
        return index;
    }

    private float findAmountFromAccountNumber(String accountNumber) {

        int index = findIndexFromAccountNumber(accountNumber);

        return records.get(index).getAmount();
    }

    private char findDepartmentNumberFromAccountNumber(String accountNumber) {
        int index;

        index = findIndexFromAccountNumber(accountNumber);

        return records.get(index).getDepartmentNumber();
    }

    private int updateAccountBalance(String accountNumber, String action, float amount) {

        int index = findIndexFromAccountNumber(accountNumber); 

        float balance = findAmountFromAccountNumber(accountNumber);

        if (action.equals("ADD")) {
            balance += amount;
            records.get(index).setAmount(balance);
        } else {
            if (amount > balance) {
                return -1;
            }

            balance -= amount;
            records.get(index).setAmount(balance);
        }

        return 1;
    }

    private int transferMoney(String accountNumberA, String accountNumberB, float amount) {

        float amountA = findAmountFromAccountNumber(accountNumberA);

        if (amountA < amount) {
            return -1;
        }

        float amountB = findAmountFromAccountNumber(accountNumberB);

        int indexA = findIndexFromAccountNumber(accountNumberA);
        int indexB = findIndexFromAccountNumber(accountNumberB);

        records.get(indexA).setAmount(amountA - amount);
        records.get(indexB).setAmount(amountB + amount);

        return 1;
    }
}
