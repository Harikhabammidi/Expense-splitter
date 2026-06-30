package model;

import java.util.List;

public class Expense {
    private static int counter = 1;

    private int id;
    private String description;
    private double totalAmount;
    private User paidBy;
    private List<Split> splits;

    public Expense(String description, double totalAmount, User paidBy, List<Split> splits) {
        this.id = counter++;
        this.description = description;
        this.totalAmount = totalAmount;
        this.paidBy = paidBy;
        this.splits = splits;
    }

    public static void setCounter(int value) {
        counter = value;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public double getTotalAmount() { return totalAmount; }
    public User getPaidBy() { return paidBy; }
    public List<Split> getSplits() { return splits; }

    @Override
    public String toString() {
        return "Expense[" + id + "] " + description + " Rs." + totalAmount + " paid by " + paidBy.getName();
    }
}