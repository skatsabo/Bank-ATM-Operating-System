
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Record {

    private int accountNumber;
    private char departmentNumber;
    private float amount;
    
    private final Semaphore available;

    public Record(int accountNumber, char departmentNumber, float amount) {
        this.accountNumber = accountNumber;
        this.departmentNumber = departmentNumber;
        this.amount = amount;
        
        available = new Semaphore(1); 
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setDepartmentNumber(char departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public char getDepartmentNumber() {
        return departmentNumber;
    }

    public float getAmount() {
        return amount;
    }

    public void availableAcquire() {
        try {
            available.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void availableRelease() {
        available.release();
    }
}
