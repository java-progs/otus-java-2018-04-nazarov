import java.util.*;

/**
 * Created by operator on 24.05.2018.
 */
public class ATMWork{

    public static void main(String args[]) {

        ATM atm = new ATM();

        atm.printState();

        Scanner sc = new Scanner(System.in);
        boolean work = true;

        while (work) {
            System.out.println();
            System.out.println("===== Main menu =====");
            System.out.println("Pleas, select action:");
            System.out.println("1: Withdrawal");
            System.out.println("2: Deposit");
            System.out.println("3: ATM Balance");
            System.out.println("4: EXIT");

            System.out.println();
            System.out.println("Enter position: ");


            int position = sc.nextInt();

            switch (position) {
                case 1:
                    System.out.println();
                    System.out.println("===== Withdrawal =====");
                    String withdrawalState = atm.getWithdrawalState();
                    if (withdrawalState.equals(atm.WITHDRAWAL_NOT_AVAILABLE)) {
                        System.out.println(atm.WITHDRAWAL_NOT_AVAILABLE);
                        break;
                    }
                    System.out.println("Enter withdrawal amount: ");
                    int amount = sc.nextInt();
                    boolean operationResult = atm.withdrawal(amount);
                    if (!operationResult) {
                        System.out.println("ATM can't withdraw cash");
                    } else {
                        System.out.println("Please take your cash");
                    }
                    break;
                case 2:
                    System.out.println();
                    System.out.println("===== Cash-in =====");
                    System.out.println("Available denominations for cash deposits: ");
                    String cashInState = atm.getCashInState();
                    System.out.println(cashInState);
                    if (cashInState.equals(atm.CASH_IN_NOT_AVAILABLE)) break;
                    TreeSet<Integer> nominals = atm.getCashInNominals();
                    HashMap<Integer, Integer> notes = new HashMap<>();
                    System.out.println("Please, enter the number of notes: ");
                    for (Integer nominal : nominals) {
                        System.out.print(nominal + " : ");
                        int count = sc.nextInt();
                        notes.put(nominal, count);
                    }

                    System.out.println("Your deposit :");
                    for (Map.Entry<Integer, Integer> pair : notes.entrySet()) {
                        System.out.print(pair.getKey() + " : " + pair.getValue() + " notes. ");
                    }
                    System.out.println();
                    boolean depositResult = atm.deposit(notes);
                    if (depositResult) {
                        System.out.println("Success deposit : " + depositResult);
                    } else {
                        System.out.println("ATM can't deposit cash");
                        System.out.println("Please take your cash");
                    }
                    break;
                case 3:
                    atm.printState();
                    break;
                case 4:
                    work = false;
                    break;
                default:
                    break;
            }
        }
    }
}
