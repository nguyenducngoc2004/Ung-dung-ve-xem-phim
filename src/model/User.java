package model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    @Override
    public String toString() {
        return username + "," + password + "," + isAdmin;
    }

    public static User fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length == 3) {
            return new User(parts[0], parts[1], Boolean.parseBoolean(parts[2]));
        }
        return null;
    }
}