/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.stream.Collectors;

/**
 *
 * @author Enter Computer
 */
public class StudentService {

    private JsonDatabaseManager dbManager;

    public StudentService() {
        dbManager = new JsonDatabaseManager();
    }

    public List<Course> getAvailableCourses(int studentID) {

        List<User> users = dbManager.getUsers();
        Student target = users.stream()
                .filter(u -> u.getUserId() == studentID && u instanceof Student)
                .map(u -> (Student) u)
                .findFirst()
                .orElse(null);

        if (target == null) {
            return new ArrayList<>();
        }

        List<Course> allCourses = new JsonDatabaseManager().loadCourses();

        return allCourses.stream()
                .filter(c -> c.getApprovalStatus() != null
                && c.getApprovalStatus().equalsIgnoreCase("APPROVED"))
                .filter(c -> target.getEnrolledCourses() == null
                || !target.getEnrolledCourses().contains(c.getId()))
                .collect(Collectors.toList());
    }

    public List<Course> getEnrolledCourses(int studentID) {
        List<User> users = dbManager.getUsers();
        Student target = null;

        for (User i : users) {
            if (i.getUserId() == studentID && i instanceof Student) {
                target = (Student) i;
                break;
            }
        }

        if (target == null) {
            System.out.println("student not found");
            return new ArrayList<>();
        }

        List<Course> allCourses = new JsonDatabaseManager().loadCourses();

        List<Course> enrolled = new ArrayList<>();

        for (Course i : allCourses) {
            if (target.getEnrolledCourses().contains(String.valueOf(i.getId()))) {
                enrolled.add(i);
            }
        }

        return enrolled;

    }

    public boolean enroll(int studentID, int courseID) {
        List<User> users = dbManager.getUsers();
        Student target = null;

        for (User i : users) {
            if (i.getUserId() == studentID && i instanceof Student) {
                target = (Student) i;
                break;
            }
        }

        if (target == null) {
            System.out.println("student not found");
            return false;
        }

        CourseService enroll = new CourseService(dbManager);
        Course course = enroll.getCourse(courseID);

        if (course == null) {
            System.out.println("course not found");
            return false;
        }

        if (target.getEnrolledCourses().contains(String.valueOf(courseID))) {
            System.out.println("Student already enrolled");
            return false;
        }

        target.getEnrolledCourses().add(String.valueOf(courseID));

        course.getEnrolledStudents().add(String.valueOf(studentID));

        JsonDatabaseManager repo = new JsonDatabaseManager();
        List<Course> allCourses = repo.loadCourses();

        for (int i = 0; i < allCourses.size(); i++) {
            if (allCourses.get(i).getId() == courseID) {
                allCourses.set(i, course);
                break;
            }
        }

        repo.saveCourses(allCourses);
        dbManager.saveUsers();

        return true;

    }

    public List<Lesson> getLessons(int studentID, int courseID) {
        List<Course> enrolled = getEnrolledCourses(studentID);

        for (Course i : enrolled) {
            if (i.getId() == courseID) {
                if (i.getLessons() == null) {
                    System.out.println("no lessons added yet");
                    return new ArrayList<>();
                }
                return i.getLessons();
            }
        }

        System.out.println("Course not found or student not enrolled");
        return new ArrayList<>();

    }

    public boolean markLessonCompleted(int studentID, int courseID, int lessonID) {
        List<User> users = dbManager.getUsers();
        Student target = null;

        for (User i : users) {
            if (i.getUserId() == studentID && i instanceof Student) {
                target = (Student) i;
                break;
            }
        }

        if (target == null) {
            System.out.println("Student not found");
            return false;
        }

        List<Course> allCourses = new JsonDatabaseManager().loadCourses();
        Course targetCourse = null;

        for (Course i : allCourses) {
            if (i.getId() == courseID) {
                targetCourse = i;
                break;
            }
        }

        if (targetCourse == null) {
            System.out.println("Course not found");
            return false;
        }

        if (!target.getEnrolledCourses().contains(String.valueOf(courseID))) {
            System.out.println("Student not enrolled in this course");
            return false;
        }

        HashMap<String, Boolean> courseProgress = target.getProgress().getOrDefault(
                String.valueOf(courseID),
                new HashMap<String, Boolean>()
        );

        courseProgress.put(String.valueOf(lessonID), true);

        target.getProgress().put(String.valueOf(courseID), courseProgress);

        dbManager.saveUsers();

        return true;
    }

    public HashMap<String, Boolean> getLessonProgress(int studentID, int courseID) {
        List<User> users = dbManager.getUsers();
        Student target = null;

        for (User i : users) {
            if (i.getUserId() == studentID && i instanceof Student) {
                target = (Student) i;
                break;
            }
        }

        if (target == null) {
            System.out.println("Student not found");
            return new HashMap<>();
        }

        return target.getProgress().getOrDefault(
                String.valueOf(courseID),
                new HashMap<String, Boolean>()
        );
    }

    public void addStudent(Student s) {
        dbManager.addUser(s);
    }
}
