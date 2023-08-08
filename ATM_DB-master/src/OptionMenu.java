import java.sql.*;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.text.DecimalFormat;
import static java.lang.System.exit;


public class OptionMenu {


    Scanner scanner = new Scanner(System.in);
    DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");
    private static Connection con = null;
    private static Statement st = null;
    private static ResultSet rs = null;

    OptionMenu() throws SQLException {
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

        st = con.createStatement();
        rs = st.executeQuery("select * from db");
    }

    public void getLogin() throws Exception{
        boolean end = false;
        int customerNumber;
        int pinNumber;
        while (!end) {
            try {
                System.out.print("\nEnter your customer number: ");
                customerNumber = scanner.nextInt();
                System.out.print("\nEnter your PIN number: ");
                pinNumber = scanner.nextInt();
                st = con.createStatement();
                rs = st.executeQuery("select * from db");
                while(rs.next()){
                    if(rs.getInt("customerId") == customerNumber){
                        if(pinNumber != rs.getInt("pin")){
                            System.out.println("Incorrect Pin Number !!");
                            break;
                        }
                        end = true;
                    }
                }
                if (!end) {
                    System.out.println("\n Customer Does not exist !!");
                    exit(0);
                }else{
                    AccountDetails ac = new AccountDetails(customerNumber, pinNumber);
                    getSaving(ac);
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid Character(s). Only Numbers.");
            }
        }
    }

    public void getSaving(AccountDetails acc) {
        boolean end = false;
        while (!end) {
            try {
                System.out.println("\nSavings Account: ");
                System.out.println(" Type 1 - View Balance");
                System.out.println(" Type 2 - Withdraw Cash");
                System.out.println(" Type 3 - Exit");
                System.out.print("Choice: ");
                int selection = scanner.nextInt();
                switch (selection) {
                    case 1 -> System.out.println("\nSavings Account Balance: " + moneyFormat.format(acc.getBalance()));
                    case 2 -> acc.getBalanceWithdrawInput();
                    case 3 -> end = true;
                    default -> System.out.println("\nInvalid Choice.");
                }
            } catch (InputMismatchException | SQLException e) {
                System.out.println("\nInvalid Choice.");
                scanner.next();
            }
        }
    }

    public void createAccount() throws SQLException{
        int cst_no;
        try {
            System.out.println("\nEnter your customer number ");
            cst_no = scanner.nextInt();

            while (rs.next()){
                if(cst_no == rs.getInt("customerId")){
                    System.out.println("Customer Already exists !!");
                    exit(0);
                }
            }
            System.out.println("\nEnter PIN to be registered");
            int pin_no = scanner.nextInt();

            String query = "insert into db values(?, ?, ?);";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, cst_no);
            st.setInt(2, pin_no);
            st.setDouble(3, 0);
            int count = st.executeUpdate();

            System.out.println("\nYour new account has been successfully registered!");
            System.out.println("\nRedirecting to login.............");
            getLogin();
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid Choice.");
            scanner.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void mainMenu() throws Exception{
        try {
            System.out.println("\n Type 1 - Login");
            System.out.println(" Type 2 - Create Account");
            System.out.println("Type 3 - Exit");
            System.out.print("\nChoice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> getLogin();
                case 2 -> createAccount();
                case 3 -> exit(0);
                default -> System.out.println("\nInvalid Choice.");
            }
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid Choice.");
            scanner.next();
        }
        System.out.println("\nThank You for using this ATM.\n");
        scanner.close();
        exit(0);
    }
}
