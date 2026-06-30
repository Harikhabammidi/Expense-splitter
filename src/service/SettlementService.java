package service;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class SettlementService {

    public List<String> settle(Map<User, Double> balances) {
        List<String> transactions = new ArrayList<>();

        // Max heap — creditors (people who get money back)
        // Sorted by highest positive balance first
        PriorityQueue<double[]> creditors = new PriorityQueue<>(
            (a, b) -> Double.compare(b[1], a[1])
        );

        // Max heap of absolute values — debtors (people who owe money)
        // Sorted by highest debt first
        PriorityQueue<double[]> debtors = new PriorityQueue<>(
            (a, b) -> Double.compare(b[1], a[1])
        );

        // Separate users into creditors and debtors
        // We store [userId, amount] in each heap
        List<User> userList = new ArrayList<>(balances.keySet());
        for (User user : userList) {
            double balance = Math.round(balances.get(user) * 100.0) / 100.0;
            if (balance > 0.01) {
                creditors.offer(new double[]{user.getId(), balance});
            } else if (balance < -0.01) {
                debtors.offer(new double[]{user.getId(), Math.abs(balance)});
            }
        }

        // Greedily settle
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            double[] creditor = creditors.poll(); // person owed the most
            double[] debtor   = debtors.poll();   // person who owes the most

            // Settle the minimum of the two amounts
            double amount = Math.min(creditor[1], debtor[1]);
            amount = Math.round(amount * 100.0) / 100.0;

            // Find actual User objects by ID
            User creditorUser = findUserById(userList, (int) creditor[0]);
            User debtorUser   = findUserById(userList, (int) debtor[0]);

            transactions.add(
                debtorUser.getName() + " pays " +
                creditorUser.getName() + " Rs." + amount
            );

            // If creditor is still owed money, put back
            double creditorLeft = Math.round((creditor[1] - amount) * 100.0) / 100.0;
            if (creditorLeft > 0.01) {
                creditors.offer(new double[]{creditor[0], creditorLeft});
            }

            // If debtor still owes money, put back
            double debtorLeft = Math.round((debtor[1] - amount) * 100.0) / 100.0;
            if (debtorLeft > 0.01) {
                debtors.offer(new double[]{debtor[0], debtorLeft});
            }
        }

        return transactions;
    }

    private User findUserById(List<User> users, int id) {
        for (User u : users) {
            if (u.getId() == id) return u;
        }
        return null;
    }
}