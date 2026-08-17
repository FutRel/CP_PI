package com.example.cp_pi;

public class User {
    int id;
    String username;
    String role;

    public User(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isSeller() { return "seller".equals(role); }
    public boolean isAdmin() { return "admin".equals(role); }
}