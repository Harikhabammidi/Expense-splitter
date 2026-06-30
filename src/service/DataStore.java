package service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class DataStore {
    private static final String DATA_DIR   = "../data/";
    private static final String USERS_FILE  = DATA_DIR + "users.json";
    private static final String GROUPS_FILE = DATA_DIR + "groups.json";

    private final Gson gson;

    public DataStore() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        // Create data directory if it doesn't exist
        new File(DATA_DIR).mkdirs();
    }

    // ── Save users ────────────────────────────────────────
    public void saveUsers(Collection<User> users) {
        try (Writer writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // ── Load users ────────────────────────────────────────
    public List<User> loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<User>>(){}.getType();
            List<User> users = gson.fromJson(reader, listType);
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ── Save groups ───────────────────────────────────────
    public void saveGroups(Collection<Group> groups) {
        try (Writer writer = new FileWriter(GROUPS_FILE)) {
            gson.toJson(groups, writer);
        } catch (IOException e) {
            System.out.println("Error saving groups: " + e.getMessage());
        }
    }

    // ── Load groups ───────────────────────────────────────
    public List<Group> loadGroups(Map<Integer, User> userMap) {
        File file = new File(GROUPS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Group>>(){}.getType();
            List<Group> groups = gson.fromJson(reader, listType);
            return groups != null ? groups : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error loading groups: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}