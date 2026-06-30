package service;

import model.*;
import java.util.*;

public class GroupService {
    private Map<Integer, Group> groups = new HashMap<>();
    private DataStore dataStore;

    public GroupService(DataStore dataStore, Map<Integer, User> userMap) {
        this.dataStore = dataStore;
        List<Group> saved = dataStore.loadGroups(userMap);
        for (Group g : saved) {
            groups.put(g.getId(), g);
        }
        int maxId = saved.stream()
                         .mapToInt(Group::getId)
                         .max().orElse(0);
        Group.setCounter(maxId + 1);
        if (!saved.isEmpty()) {
            System.out.println("Loaded " + saved.size() + " groups from file.");
        }
    }

    public Group createGroup(String name) {
        Group group = new Group(name);
        groups.put(group.getId(), group);
        dataStore.saveGroups(groups.values());
        System.out.println("Created: " + group);
        return group;
    }

    public void addUserToGroup(Group group, User user) {
        if (group.getMembers().contains(user)) {
            System.out.println(user.getName() + " is already in " + group.getName());
            return;
        }
        group.addMember(user);
        dataStore.saveGroups(groups.values());
        System.out.println("Added " + user.getName() + " to " + group.getName());
    }

    public Group getGroupById(int id) {
        Group group = groups.get(id);
        if (group == null) System.out.println("No group found with ID: " + id);
        return group;
    }

    public Collection<Group> getAllGroups() { return groups.values(); }

    public void listGroups() {
        if (groups.isEmpty()) { System.out.println("No groups yet."); return; }
        System.out.println("--- All Groups ---");
        for (Group g : groups.values()) {
            System.out.println(g);
            for (User u : g.getMembers()) System.out.println("   - " + u.getName());
        }
    }
}