package server;

import model.User;
import utils.FileHandler;
import java.util.List;

public class UserManager {
    private List<User> users;
    private static final String USERS_FILE = "data/users.txt";

    public UserManager() {
        loadUsers();
    }

    private void loadUsers() {
        users = FileHandler.readUsers(USERS_FILE);
        // Add default admin user if none exists
        if (users.stream().noneMatch(User::isAdmin)) {
            users.add(new User("admin", "admin123", true));
            saveUsers();
        }
    }

    private void saveUsers() {
        FileHandler.writeUsers(USERS_FILE, users);
    }

    public boolean registerUser(String username, String password) {
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            return false; // Username already exists
        }
        users.add(new User(username, password, false));
        saveUsers();
        return true;
    }

    public User loginUser(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<User> getUsers() {
        return users;
    }
}