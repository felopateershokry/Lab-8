package skillfrog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author GAMING
 */
public class JsonDatabaseManager {

    private ArrayList<User> users;
    private ArrayList<Course> courses;
    private final String FILE_Courses = "courses.json";
    private final Gson gson1 = new Gson();

    private final Gson gson;

    public JsonDatabaseManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        users = new ArrayList<>();
        courses = new ArrayList<>();

        loadUsers();
    }

    private void loadUsers() {
        try {
            File file = new File("users.json");
            if (!file.exists()) {
                file.createNewFile();
                saveUsers();
                return;
            }

            FileReader reader = new FileReader(file);
            JsonArray jsonArr = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();

            for (JsonElement element : jsonArr) {
                JsonObject obj = element.getAsJsonObject();
                String role = obj.get("role").getAsString();

                if (role.equals("student")) {
                    Student s = gson.fromJson(obj, Student.class);
                    users.add(s);
                } else {
                    Instructor ins = gson.fromJson(obj, Instructor.class);
                    users.add(ins);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading users.json");
        }
    }

    public void saveUsers() {
        try (FileWriter writer = new FileWriter("users.json")) {
            JsonArray arr = new JsonArray();
            for (User u : users) {
                JsonElement obj = gson.toJsonTree(u);
                arr.add(obj);
            }
            gson.toJson(arr, writer);
        } catch (Exception ignored) {
        }
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public User getUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public int generateUserId() {
        return users.size() + 1;
    }

    public List<Course> loadCourses() {
        try (FileReader reader = new FileReader(FILE_Courses)) {
            return gson1.fromJson(reader, new TypeToken<List<Course>>() {
            }.getType());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void saveCourses(List<Course> courses) {
        try (FileWriter writer = new FileWriter(FILE_Courses)) {
            gson1.toJson(courses, writer);
        } catch (Exception e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    public void saveCourses(Course courses) {
        try (FileWriter writer = new FileWriter(FILE_Courses)) {
            gson1.toJson(courses, writer);
        } catch (Exception e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

}
