/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lenovo
 */
public class LoadInstructorCoursesToTable {

    public static DefaultTableModel coursesTableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Course ID", "Title", "Description", "Status"}
    );

    public static void loadInstructorCourses(String coursesFile, int instructorId) {
        try {
            Gson gson = new Gson();

            List<Course> courses = gson.fromJson(
                    new FileReader(coursesFile),
                    new TypeToken<List<Course>>() {
                    }.getType()
            );

            coursesTableModel.setRowCount(0); // Clear old rows

            for (Course c : courses) {
                if (c.getInstructorId() == instructorId) {
                    coursesTableModel.addRow(new Object[]{
                        c.getId(),
                        c.getTitle(),
                        c.getDescription(),
                        c.getApprovalStatus()
                    });
                }
            }

            System.out.println("Courses loaded for instructor " + instructorId);

        } catch (IOException e) {
            System.err.println("Error loading courses file: " + e.getMessage());
        }
    }
}
