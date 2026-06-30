package service;

import model.Expense;
import model.Group;
import model.Split;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseService {

    public Expense addEqualExpense(Group group, String description,
                                   double totalAmount, User paidBy) {

        List<User> members = group.getMembers();

        if (!members.contains(paidBy)) {
            System.out.println("Error: " + paidBy.getName() + " is not in this group.");
            return null;
        }

        double share = totalAmount / members.size();
        // Round to 2 decimal places to avoid floating point mess
        share = Math.round(share * 100.0) / 100.0;

        List<Split> splits = new ArrayList<>();
        for (User member : members) {
            splits.add(new Split(member, share));
        }

        Expense expense = new Expense(description, totalAmount, paidBy, splits);
        group.addExpense(expense);

        System.out.println("Added expense: " + expense);
        System.out.println("Split equally: Rs." + share + " per person");
        return expense;
    }

    public Expense addCustomExpense(Group group, String description,
                                    double totalAmount, User paidBy,
                                    Map<User, Double> customSplits) {

        if (!group.getMembers().contains(paidBy)) {
            System.out.println("Error: " + paidBy.getName() + " is not in this group.");
            return null;
        }

        // Validate that split amounts add up to total
        double sum = 0;
        for (double amt : customSplits.values()) sum += amt;
        if (Math.abs(sum - totalAmount) > 0.01) {
            System.out.println("Error: Split amounts (Rs." + sum +
                               ") don't add up to total (Rs." + totalAmount + ")");
            return null;
        }

        List<Split> splits = new ArrayList<>();
        for (Map.Entry<User, Double> entry : customSplits.entrySet()) {
            splits.add(new Split(entry.getKey(), entry.getValue()));
        }

        Expense expense = new Expense(description, totalAmount, paidBy, splits);
        group.addExpense(expense);

        System.out.println("Added custom expense: " + expense);
        return expense;
    }

    // THE KEY METHOD — feeds directly into SettlementService
    public Map<User, Double> computeBalances(Group group) {
        Map<User, Double> balances = new HashMap<>();

        // Initialize every member at 0
        for (User member : group.getMembers()) {
            balances.put(member, 0.0);
        }

        for (Expense expense : group.getExpenses()) {
            User payer = expense.getPaidBy();

            // Payer gets credited the full amount
            balances.put(payer, balances.get(payer) + expense.getTotalAmount());

            // Each person in splits gets debited their share
            for (Split split : expense.getSplits()) {
                User user = split.getUser();
                balances.put(user, balances.get(user) - split.getAmount());
            }
        }

        return balances;
    }

    public void printBalances(Group group) {
        Map<User, Double> balances = computeBalances(group);
        System.out.println("--- Balances in " + group.getName() + " ---");
        for (Map.Entry<User, Double> entry : balances.entrySet()) {
            double bal = Math.round(entry.getValue() * 100.0) / 100.0;
            String status;
            if (bal > 0)      status = "gets back Rs." + bal;
            else if (bal < 0) status = "owes Rs." + Math.abs(bal);
            else               status = "is settled";
            System.out.println("  " + entry.getKey().getName() + " " + status);
        }
    }
}