import java.util.Scanner;

/* We have defined the following methods for each state in our vending machine
*       But for each state, only some of the methods are necessary.
*/
interface State {
    void insertCoin() throws Exception;
    void ejectMoney() throws Exception;
    void selectDrink() throws Exception;
    void ejectChange() throws Exception;
    void dispenseDrink() throws Exception;
}

/*
*   We can define the following states for the Vending Machine:-
*   InsertCoinState — Machine ready to accept coins. Accepted coins are 10c, 20c and 50c
*   EjectMoneyState — Machine has collected the coins but the user wants to eject them
*   SelectDrinkState — User has inserted the coins and now has to select the type of drink
*   DispenseDrinkState — Dispense the item upon successful validation of entered cash & 
*                           the price of the selected item in inventory
*   EjectChangeState — The user has collected the drink and now the machine has to eject the 
*                           extra changes inserted by the user
*/

class InsertCoinState implements State {
    Scanner scanner;
    private VendingMachine028_032 vendingMachine;

    public InsertCoinState(VendingMachine028_032 vendingMachine, Scanner scanner) throws Exception {
        this.vendingMachine = vendingMachine;
        this.scanner = scanner;
        // Calling on the initial state upon starting of the vending machine
        insertCoin();
    }

    @Override
    public void insertCoin() throws Exception {
        System.out.println("Prices:\n1.Coffee - 120\n2.Cappuccino - 150"); // printing prices of items
        System.out.println("Insert coins (seperated by a single whitespace):");
        try {
            String input = scanner.nextLine();
            String[] coins = input.split(" ");
            for (String coin : coins) {
                if (coin.equals("10") || coin.equals("20") || coin.equals("50")) {
                    vendingMachine.addBalance(Integer.parseInt(coin));
                } else {
                    System.out.println("Invalid coin: " + coin);
                    System.out.println("Ejecting all inserted coins...");
                    // If the user inserts invalid coins, the machine will eject all the coins and go back
                    //      to the starting state (InsertCoinState)
                    vendingMachine.setState(new InsertCoinState(vendingMachine, scanner));
                }
            }
            
            int option = 0;
            while (option != 1 && option != 2) {
                try {
                    System.out.println("1. Eject money, 2. Select drink");
                    input = scanner.nextLine();
                    option = Integer.parseInt(input);
                    if (option == 1) {
                        // eject money selected
                        vendingMachine.setState(new EjectMoneyState(vendingMachine, scanner));
                    } else if (option == 2) {
                        // select drinks selected
                        vendingMachine.setState(new SelectDrinkState(vendingMachine, scanner));
                    }
                } catch (Exception e) {
                    System.out.println("Invalid option");
                }
            }
             
        } catch (NumberFormatException e) {
            System.out.println("Invalid option");
            vendingMachine.setState(new InsertCoinState(vendingMachine, scanner));
        }
    }

    @Override
    public void ejectMoney() throws Exception{
        throw new Exception("Incorrect state: ejectMoney()");
    }

    @Override
    public void selectDrink() throws Exception {
        throw new Exception("Incorrect state: selectDrink()");   
    }

    @Override
    public void ejectChange() throws Exception {
        throw new Exception("Incorrect state: ejectChange()");
    }

    @Override
    public void dispenseDrink() throws Exception {
        throw new Exception("Incorrect state: dispenseDrink()");
    }
}

class EjectMoneyState implements State {
    Scanner scanner;
    private VendingMachine028_032 vendingMachine;

    public EjectMoneyState(VendingMachine028_032 vendingMachine, Scanner scanner) throws Exception {
        this.vendingMachine = vendingMachine;
        this.scanner = scanner;
        ejectMoney();
    }

    @Override
    public void insertCoin() throws Exception {
        throw new Exception("Incorrect state: insertCoin()");
    }

    @Override
    public void ejectMoney() throws Exception {
        // ejecting all the money inserted by the user
        System.out.println("Ejecting money: " + vendingMachine.getBalance());
        vendingMachine.emptyBalance();
        vendingMachine.setState(new InsertCoinState(vendingMachine, scanner));
    }

    @Override
    public void selectDrink() throws Exception {
        throw new Exception("Incorrect state: selectDrink()");
    }

    @Override
    public void ejectChange() throws Exception {
        throw new Exception("Incorrect state: ejectChange()");
    }

    @Override
    public void dispenseDrink() throws Exception {
        throw new Exception("Incorrect state: dispenseDrink()");
    }
}

class SelectDrinkState implements State {
    Scanner scanner;
    private VendingMachine028_032 vendingMachine;

    public SelectDrinkState(VendingMachine028_032 vendingMachine, Scanner scanner) throws Exception {
        this.vendingMachine = vendingMachine;
        this.scanner = scanner;
        selectDrink();
    }

    @Override
    public void insertCoin() throws Exception {
        throw new Exception("Incorrect state: insertCoin()");
    }

    @Override
    public void ejectMoney() throws Exception {
        throw new Exception("Incorrect state: ejectMoney()");
    }

    @Override
    public void selectDrink() throws Exception {
        boolean stockOut = false;
        // printing the available drink options
        System.out.println("Select drink (1. Coffee, 2. Cappuccino):");
        try{
            String input = scanner.nextLine();
            int drink = Integer.parseInt(input);
            if (drink == 1) {   // coffee selected
                if(vendingMachine.getCoffeeStock() > 0){
                    if (vendingMachine.getBalance() >= 120) {
                        vendingMachine.deductBalance(120);
                        System.out.println("Dispensing coffee");
                        vendingMachine.unstockCoffee();;
                    } else {
                        // Machine will eject money and go back to start state
                        System.out.println("Insufficient balance");
                        vendingMachine.setState(new EjectMoneyState(vendingMachine, scanner));
                    }
                } else {
                    stockOut = true;
                    System.out.println("Coffee Stock out");
                }
            } else if (drink == 2) { // cappuccino selected
                if(vendingMachine.getCappuccinoStock() > 0){
                    if (vendingMachine.getBalance() >= 150) {
                        vendingMachine.deductBalance(150);
                        System.out.println("Dispensing cappuccino");
                        vendingMachine.unstockCappuccino();
                    } else {
                        // Machine will eject money and go back to start state
                        System.out.println("Insufficient balance");
                        vendingMachine.setState(new EjectMoneyState(vendingMachine, scanner));
                    }
                } else{
                    stockOut = true;
                    System.out.println("Cappuccino Stock out");
                }
            } else {
                System.out.println("Invalid drink");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid drink");
        }
        if (vendingMachine.getBalance() > 0 && stockOut) {
            vendingMachine.setState(new EjectChangeState(vendingMachine, scanner));
            vendingMachine.setState(new InsertCoinState(vendingMachine, scanner));
        } else if (vendingMachine.getBalance() > 0 && !stockOut) {
            vendingMachine.setState(new EjectChangeState(vendingMachine, scanner));
            vendingMachine.setState(new DispenseDrinkState(vendingMachine, scanner));
        }
        else {
            vendingMachine.setState(new DispenseDrinkState(vendingMachine, scanner));
        }
    }

    @Override
    public void ejectChange() throws Exception {
        throw new Exception("Incorrect state: ejectChange()");
    }

    @Override
    public void dispenseDrink() throws Exception {
        throw new Exception("Incorrect state: dispenseDrink()");
    }
}

class EjectChangeState implements State {
    Scanner scanner;
    private VendingMachine028_032 vendingMachine;

    public EjectChangeState(VendingMachine028_032 vendingMachine, Scanner scanner) throws Exception {
        this.vendingMachine = vendingMachine;
        this.scanner = scanner;
        ejectChange();
    }

    @Override
    public void insertCoin() throws Exception {
        throw new Exception("Incorrect state: insertCoin()");
    }

    @Override
    public void ejectMoney() throws Exception {
        throw new Exception("Incorrect state: ejectMoney()");
    }

    @Override
    public void selectDrink() throws Exception {
        throw new Exception("Incorrect state: selectDrink()");
    }

    @Override
    public void ejectChange() throws Exception {
        System.out.println("Ejecting change: " + vendingMachine.getBalance());
        vendingMachine.emptyBalance();
    }

    @Override
    public void dispenseDrink() throws Exception {
        throw new Exception("Incorrect state: dispenseDrink()");
    }
}

class DispenseDrinkState implements State {
    Scanner scanner;
    private VendingMachine028_032 vendingMachine;

    public DispenseDrinkState(VendingMachine028_032 vendingMachine, Scanner scanner) throws Exception {
        this.vendingMachine = vendingMachine;
        this.scanner = scanner;
        dispenseDrink();
    }

    @Override
    public void insertCoin() throws Exception {
        throw new Exception("Incorrect state: insertCoin()");
    }

    @Override
    public void ejectMoney() throws Exception {
        throw new Exception("Incorrect state: ejectMoney()");
    }

    @Override
    public void selectDrink() throws Exception {
        throw new Exception("Incorrect state: selectDrink()");
    }

    @Override
    public void ejectChange() throws Exception {
        throw new Exception("Incorrect state: ejectChange()");
    }

    @Override
    public void dispenseDrink() throws Exception {
        System.out.println("Dispensed drink, please take it");
        vendingMachine.setState(new InsertCoinState(vendingMachine, scanner));
    }
}

// Main tester class for the vending machine solution
public class VendingMachine028_032 {
    private int balance = 0;
    private State state;
    // each item has 5 elements in stock initially
    private int coffeeStock = 5;
    private int cappuccinoStock = 5;
    Scanner scanner = new Scanner(System.in);
    public void addBalance(int amount) {
        balance += amount;
    }

    public void deductBalance(int amount) {
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }

    public void emptyBalance() {
        balance = 0;
    }

    public int getCoffeeStock() {
        return coffeeStock;
    }

    public int getCappuccinoStock() {
        return cappuccinoStock;
    }

    public void unstockCoffee() {
        coffeeStock -= 1;
    }

    public void unstockCappuccino() {
        cappuccinoStock -= 1;
    }

    public VendingMachine028_032() throws Exception {
        State insertCoinState = new InsertCoinState(this, scanner);
    }

    public void setState(State state) {
        this.state = state;
    }
    public static void main(String[] args) {
        try {
            // starting the vending machine
            VendingMachine028_032 vendingMachine = new VendingMachine028_032();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
