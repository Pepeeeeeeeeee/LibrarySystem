package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.Flags;

public class User {
    private final int id;
    private final String username;
    private final String email;
    private final String password;
    private final Flags flags; // optional

    public User(int id, String username, String email, String password, Flags flags) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.flags = flags;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Flags flags() { return flags; }
}