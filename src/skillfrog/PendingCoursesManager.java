package skillfrog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.table.DefaultTableModel;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PendingCoursesManager {

    public static DefaultTableModel tableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Status", "Course ID", "Instructor ID", "Title", "Description"}
    );

    public static void loadPendingCourses(String coursesFile) {
        try {
            Gson gson = new Gson();

            List<Course> courses = gson.fromJson(
                    new FileReader(coursesFile),
                    new TypeToken<List<Course>>() {
                    }.getType()
            );

            tableModel.setRowCount(0); // Clear old rows

            for (Course c : courses) {
                if ("PENDING".equalsIgnoreCase(c.getApprovalStatus())) {
                    tableModel.addRow(new Object[]{
                        c.getApprovalStatus(),
                        c.getId(),
                        c.getInstructorId(), // instructor ID
                        c.getTitle(),
                        c.getDescription()
                    });
                }
            }

            System.out.println("Pending courses loaded successfully!");

        } catch (IOException e) {
            System.err.println("Error loading courses file: " + e.getMessage());
        }
    }
}
