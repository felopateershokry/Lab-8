/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillforg.Tables;

/**
 *
 * @author Lenovo
 */
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.table.DefaultTableModel;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import skillforg.Course;
import skillforg.Student;

public class CourseEnrollmentManager {

    public static DefaultTableModel tableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Course ID", "Student ID", "Student Name"}
    );

    public static void loadEnrollments(String coursesFile, String usersFile, int instructorId) {
        try {
            Gson gson = new Gson();

            // Load all courses
            List<Course> courses = gson.fromJson(
                    new FileReader(coursesFile),
                    new TypeToken<List<Course>>() {
                    }.getType()
            );

            // Load all students
            List<Student> users = gson.fromJson(
                    new FileReader(usersFile),
                    new TypeToken<List<Student>>() {
                    }.getType()
            );

            tableModel.setRowCount(0);

            Collections.sort(courses, Comparator.comparingInt(Course::getId));

            for (Student s : users) {
                for (String enrolledCourseId : s.getEnrolledCourses()) {

                    int courseId = Integer.parseInt(enrolledCourseId);

                    // Check if the course exists AND belongs to this instructor
                    boolean courseExistsForInstructor = courses.stream()
                            .anyMatch(c -> c.getId() == courseId && instructorId == c.getInstructorId());

                    if (courseExistsForInstructor) {
                        tableModel.addRow(new Object[]{
                            courseId,
                            s.getUserId(),
                            s.getUsername()
                        });
                    }
                }
            }

            System.out.println("Enrollment data loaded for instructor " + instructorId);

        } catch (IOException e) {
            System.err.println("Error loading files: " + e.getMessage());
        }
    }

}
