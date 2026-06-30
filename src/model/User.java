package model;

public class User {
    private static int counter = 1;

    private int id;
    private String name;
    private String email;

    public User(String name, String email) {
        this.id = counter++;
        this.name = name;
        this.email = email;
    }

    public static void setCounter(int value) {
        counter = value;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "User[" + id + "] " + name + " (" + email + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User u = (User) o;
        return this.id == u.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}