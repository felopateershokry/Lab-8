/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

/**
 *
 * @author Lenovo
 */
import java.util.ArrayList;
import java.util.List;

public class Course {

    private int id;
    private String title;
    private String description;
    private List<Lesson> lessons = new ArrayList<>();
    private List<String> enrolledStudents = new ArrayList<>();
    private String approvalStatus = "pending";
    private int instructorId;

    public Course(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<String> getEnrolledStudents() {
        return enrolledStudents;
    }
}
