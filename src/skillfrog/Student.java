/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author GAMING
 */
public class Student extends User {

    private ArrayList<String> enrolledCourses;
    private HashMap<String, HashMap<String, Boolean>> progress;

    public Student(int id, String name, String email, String pass) {
        super(id, name, email, pass, "student");
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
    }

    public ArrayList<String> getEnrolledCourses() {
        if (enrolledCourses == null) {
            enrolledCourses = new ArrayList<>();
        }
        return enrolledCourses;
    }

    public HashMap<String, HashMap<String, Boolean>> getProgress() {
        return progress;
    }

    public void setProgress(HashMap<String, HashMap<String, Boolean>> progress) {
        this.progress = progress;
    }

    public void setEnrolledCourses(ArrayList<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void enrollCourse(String Id) {
        if (!enrolledCourses.contains(Id)) {
            enrolledCourses.add(Id);
        }
    }

    public void markLessonCompleted(String courseId, String lessonId) {
        HashMap<String, Boolean> courseProgress = progress.getOrDefault(courseId, new HashMap<>());
        courseProgress.put(lessonId, true);
        progress.put(courseId, courseProgress);
    }

}
