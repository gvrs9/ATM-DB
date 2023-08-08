import java.sql.*;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AccountDetails {

    //parameters
    private int customerId;
    private int pin;
    private double balance;

    Scanner scanner = new Scanner(System.in);

    //Formatting money string input
    DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");

    Connection con = null;
    Statement st = null;

    //constructor for AccountDetails with customerId, pin
    public AccountDetails(int customerId, int pin) throws SQLException {

        //db parameters
        String url = "jdbc:mysql://localhost:3306/atm";
        String uname = "root";
        String pass = "1441";

        //Establish Connection
        try {
            con = DriverManager.getConnection(url, uname, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        this.customerId = customerId;
        this.pin = pin;

        //Fetch Data
        String query = "select * from db where customerId = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, customerId);
        ResultSet rs = st.executeQuery();
        while(rs.next()){
            balance = rs.getDouble("balance");
        }
    }

    public int setCustomerId(int customerId) {
        this.customerId = customerId;
        return customerId;
    }

    public int getCustomerId(){
        return customerId;
    }

    public int setPin(int pin){
        this.pin = pin;
        return pin;
    }

    public int getPin(){
        return pin;
    }

    public double getBalance(){
        return balance;
    }

    //calculating balance after withdrawal
    public double calcBalanceWithdraw(double amount) throws SQLException{
        balance = (balance - amount);

        String query = "insert into db values(?, ?, ?)";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, customerId);
        st.setInt(2, pin);
        st.setDouble(3, balance);

        return balance;
    }

    //calculation balance after deposit
    public double calcBalanceDeposit(double amount) throws SQLException{
        balance = balance + amount;

        String query = "insert into db values(?, ?, ?)";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1, customerId);
        st.setInt(2, pin);
        st.setDouble(3, balance);

        return balance;
    }

    //get withdrawal input
    public void getBalanceWithdrawInput() throws SQLException{
        boolean flag = false;
        while (!flag) {
            try {
                System.out.println("\nCurrent Account Balance: " + moneyFormat.format(balance));
                System.out.print("\nAmount you want to withdraw from Savings Account: ");
                double amount = scanner.nextDouble();
                if ((balance - amount) >= 0 && amount >= 0) {
                    calcBalanceWithdraw(amount);
                    System.out.println("\nCurrent Savings Account Balance: " + moneyFormat.format(balance));
                    flag = true;
                } else {
                    System.out.println("\nBalance Cannot Be Negative.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid Choice.");
                scanner.next();
            }
        }
    }
}
