package model;

public class Split {
    private User user;
    private double amount;

    public Split(User user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public User getUser() { return user; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return user.getName() + " owes Rs." + amount;
    }
}