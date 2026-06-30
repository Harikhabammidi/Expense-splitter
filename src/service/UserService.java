package service;

import model.User;
import java.util.*;

public class UserService {
    private Map<Integer, User> users = new HashMap<>();
    private DataStore dataStore;

    public UserService(DataStore dataStore) {
        this.dataStore = dataStore;
        // Load existing users from file on startup
        List<User> saved = dataStore.loadUsers();
        for (User u : saved) {
            users.put(u.getId(), u);
        }
        // Sync the static counter so IDs don't repeat
        int maxId = saved.stream()
                         .mapToInt(User::getId)
                         .max().orElse(0);
        User.setCounter(maxId + 1);
        if (!saved.isEmpty()) {
            System.out.println("Loaded " + saved.size() + " users from file.");
        }
    }

    public User createUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getId(), user);
        dataStore.saveUsers(users.values()); // auto-save
        System.out.println("Created: " + user);
        return user;
    }

    public User getUserById(int id) {
        User user = users.get(id);
        if (user == null) System.out.println("No user found with ID: " + id);
        return user;
    }

    public Map<Integer, User> getUserMap() { return users; }

    public Collection<User> getAllUsers() { return users.values(); }

    public void listUsers() {
        if (users.isEmpty()) { System.out.println("No users yet."); return; }
        System.out.println("--- All Users ---");
        for (User u : users.values()) System.out.println(u);
    }
}