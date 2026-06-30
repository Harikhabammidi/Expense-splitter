package model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private static int counter = 1;

    private int id;
    private String name;
    private List<User> members;
    private List<Expense> expenses;

    public Group(String name) {
        this.id = counter++;
        this.name = name;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public static void setCounter(int value) {
        counter = value;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<User> getMembers() { return members; }
    public List<Expense> getExpenses() { return expenses; }

    @Override
    public String toString() {
        return "Group[" + id + "] " + name + " (" + members.size() + " members)";
    }
}