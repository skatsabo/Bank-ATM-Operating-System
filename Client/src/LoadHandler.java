
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LoadHandler {

    private final String filename;
    private String departmentNumber;
    
    private final int loadSize = 20000;
    private final int serverRecordsSize = 1000;
    private final File file;
    private PrintWriter out;
    private Scanner in;

    public LoadHandler(String filename, String departmentNumber) {
        this.filename = filename;
        this.departmentNumber = departmentNumber;

        file = new File(filename);
    }

    public void populateLoadFromFile(ArrayList<String> load) {
        if (!file.exists()) {
            createFile();
        }

        loadFromFile(load);
    }

    private void createFile() {
        int  accountNumber;
        int accountNumberA;
        int accountNumberB;
        float amount;
        String query;
        String action;

        try {
            out = new PrintWriter(file);

            out.write("departmentNumber-queryType-[queryDetails-...]\n");

            for (int i = 1; i <= loadSize; i++) {
                query = getRandQuery();

                if (query.equals("Q1")) {
                    accountNumber = getRandAccountNumberForQ1Q2();
                    out.append(departmentNumber + "-" + query + "-" + accountNumber + "\n");
                } else if (query.equals("Q2")) {
                    accountNumber = getRandAccountNumberForQ1Q2();
                    action = getRandAction();
                    amount = getRandAmount();
                    out.append(departmentNumber + "-" + query + "-" + accountNumber + "-" + action + "-" + amount + "\n");
                } else if (query.equals("Q3")) {
                    accountNumberA = getRandAccountNumberForQ3Q4();
                    accountNumberB = getRandAccountNumberForQ3Q4();
                    amount = getRandAmount();
                    out.append(departmentNumber + "-" + query + "-" + accountNumberA + "-" + accountNumberB + "-" + amount + "\n");
                } else if (query.equals("Q4")) {
                    out.append(departmentNumber + "-" + query + "\n");
                }
            }
            out.close();
            System.out.println("Department " + departmentNumber + " >> Create file completed...");
        } catch (FileNotFoundException ex) {
            System.out.println("Department " + departmentNumber + " >> Error on create...");
        }
    }

    private void loadFromFile(ArrayList<String> load) {
        try {
            in = new Scanner(file);

            if (in.hasNext()) {
                in.nextLine();
            }

            while (in.hasNext()) {
                load.add(in.nextLine());
            }

            in.close();

            System.out.println("Department " + departmentNumber + " >> Load from file completed...");
        } catch (FileNotFoundException ex) {
            System.out.println("Department " + departmentNumber + " >> Error on load...");
        }
    }

    private int getRandAccountNumberForQ1Q2() {
        Random r = new Random();

        int p = Math.abs(r.nextInt() % 100) + 1;

        if (departmentNumber.equals("1")) {
            if (p <= 90) {
                return Math.abs(r.nextInt() % serverRecordsSize / 2) + 1;
            } else {
                return Math.abs(r.nextInt() % serverRecordsSize / 2) + 1 + serverRecordsSize / 2;
            }
        } else {
            if (p <= 90) {
                return Math.abs(r.nextInt() % serverRecordsSize / 2) + 1 + serverRecordsSize / 2;
            } else {
                return Math.abs(r.nextInt() % serverRecordsSize / 2) + 1;
            }
        }
    }

    private int getRandAccountNumberForQ3Q4() {
        Random r = new Random();

        if (departmentNumber.equals("1")) {
            return Math.abs(r.nextInt() % serverRecordsSize / 2) + 1;
        } else {
            return Math.abs(r.nextInt() % serverRecordsSize / 2) + 1 + serverRecordsSize / 2;
        }
    }

    private String getRandQuery() { 
        Random r = new Random();

        int randQuery = Math.abs((r.nextInt() % 100) + 1);

        if (randQuery <= 40) {
            return "Q1";
        }

        if (randQuery <= 80) {
            return "Q2";
        }

        if (randQuery <= 99) {
            return "Q3";
        }
        return "Q4"; // randQuery == 100
    }

    private String getRandAction() {
        int randAction;
        Random r = new Random();

        randAction = Math.abs(r.nextInt() % 2);

        if (randAction == 1) {
            return "ADD";
        }

        return "REMOVE";
    }

    private float getRandAmount() {
        Random r = new Random();

        return Math.abs(r.nextFloat()) * 1000;
    }
}
