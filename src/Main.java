import model.*;
import service.*;
import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static DataStore dataStore                 = new DataStore();
    static UserService userService             = new UserService(dataStore);
    static GroupService groupService           = new GroupService(dataStore, userService.getUserMap());
    static ExpenseService expenseService       = new ExpenseService();
    static SettlementService settlementService = new SettlementService();

    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("   Expense Splitter");
        System.out.println("================================");

        while (true) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1  -> addUser();
                case 2  -> listUsers();
                case 3  -> createGroup();
                case 4  -> listGroups();
                case 5  -> addUserToGroup();
                case 6  -> addExpense();
                case 7  -> viewBalances();
                case 8  -> settleGroup();
                case 0  -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice. Try again.");
            }
            System.out.println();
        }
    }

    static void printMenu() {
        System.out.println("--------------------------------");
        System.out.println("1. Add user");
        System.out.println("2. List all users");
        System.out.println("3. Create group");
        System.out.println("4. List all groups");
        System.out.println("5. Add user to group");
        System.out.println("6. Add expense to group");
        System.out.println("7. View balances");
        System.out.println("8. Settle group");
        System.out.println("0. Exit");
        System.out.println("--------------------------------");
    }

    static void addUser() {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        userService.createUser(name, email);
    }

    static void listUsers() {
        userService.listUsers();
    }

    static void createGroup() {
        System.out.print("Group name: ");
        String name = sc.nextLine().trim();
        groupService.createGroup(name);
    }

    static void listGroups() {
        groupService.listGroups();
    }

    static void addUserToGroup() {
        listGroups();
        int gid = readInt("Enter group ID: ");
        Group group = groupService.getGroupById(gid);
        if (group == null) return;

        listUsers();
        int uid = readInt("Enter user ID: ");
        User user = userService.getUserById(uid);
        if (user == null) return;

        groupService.addUserToGroup(group, user);
    }

    static void addExpense() {
        listGroups();
        int gid = readInt("Enter group ID: ");
        Group group = groupService.getGroupById(gid);
        if (group == null) return;

        if (group.getMembers().isEmpty()) {
            System.out.println("Group has no members. Add members first.");
            return;
        }

        System.out.print("Description: ");
        String desc = sc.nextLine().trim();
        double amount = readDouble("Total amount: Rs.");

        System.out.println("Members in group:");
        for (User u : group.getMembers()) {
            System.out.println("  " + u);
        }
        int uid = readInt("Who paid? Enter user ID: ");
        User paidBy = userService.getUserById(uid);
        if (paidBy == null) return;

        System.out.println("Split type:");
        System.out.println("  1. Equal split");
        System.out.println("  2. Custom split");
        int splitChoice = readInt("Enter choice: ");

        if (splitChoice == 1) {
            expenseService.addEqualExpense(group, desc, amount, paidBy);
        } else if (splitChoice == 2) {
            Map<User, Double> customSplits = new HashMap<>();
            System.out.println("Enter amount owed by each member:");
            for (User u : group.getMembers()) {
                double share = readDouble("  " + u.getName() + " owes Rs.: ");
                customSplits.put(u, share);
            }
            expenseService.addCustomExpense(group, desc, amount, paidBy, customSplits);
        } else {
            System.out.println("Invalid split type.");
        }
    }

    static void viewBalances() {
        listGroups();
        int gid = readInt("Enter group ID: ");
        Group group = groupService.getGroupById(gid);
        if (group == null) return;
        expenseService.printBalances(group);
    }

    static void settleGroup() {
        listGroups();
        int gid = readInt("Enter group ID: ");
        Group group = groupService.getGroupById(gid);
        if (group == null) return;

        Map<User, Double> balances = expenseService.computeBalances(group);
        List<String> transactions = settlementService.settle(balances);

        if (transactions.isEmpty()) {
            System.out.println("Everyone is settled in " + group.getName() + "!");
            return;
        }

        System.out.println("--- Minimum transactions to settle " + group.getName() + " ---");
        int i = 1;
        for (String t : transactions) {
            System.out.println("  " + i++ + ". " + t);
        }
        System.out.println("Total transactions needed: " + transactions.size());
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }
}