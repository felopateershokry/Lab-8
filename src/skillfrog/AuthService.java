/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

/**
 *
 * @author GAMING
 */
public class AuthService {

    private JsonDatabaseManager db;

    public AuthService(JsonDatabaseManager db) {
        this.db = db;
    }

    public String signup(String username, String email, String password, String role) {

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return "Please fill all fields.";
        }

        if (!email.contains("@") || !email.contains(".")) {
            return "Invalid email address.";
        }

        if (db.getUserByEmail(email) != null) {
            return "Email already exists.";
        }

        String hashed = PasswordUtils.hashPassword(password);

        int id = db.generateUserId();

        if (role.equals("student")) {
            Student s = new Student(id, username, email, hashed);
            db.addUser(s);

        } else if (role.equals("instructor")) {
            Instructor i = new Instructor(id, username, email, hashed);
            db.addUser(i);
        }

        return "SUCCESS";
    }

    public User login(String email, String password) {

        User user = db.getUserByEmail(email);
        if (user == null) {
            return null;
        }

        String hashed = PasswordUtils.hashPassword(password);

        if (user.getPasswordHash().equals(hashed)) {
            return user;
        }

        return null;
    }
}
