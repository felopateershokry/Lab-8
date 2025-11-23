/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

/**
 *
 * @author GAMING
 */
public abstract class User {

    protected int userId;
    protected String role;
    protected String username;
    protected String email;
    protected String passwordHash;

    public User() {

    }
    // super(id, name, email, pass ,"instructor");

    public User(int id, String username, String email, String passwordHash, String role) {
        this.userId = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;

    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    int getId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
