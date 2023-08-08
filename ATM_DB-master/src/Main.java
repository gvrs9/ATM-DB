public class Main {

    public static void main(String[] args) throws Exception {
        OptionMenu optionMenu = new OptionMenu();
        introduction();
        optionMenu.mainMenu();
    }

    public static void introduction() {
        System.out.println("Welcome to the ATM Project!");
    }
}