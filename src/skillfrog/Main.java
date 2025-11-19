package skillfrog;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        StudentService studentService = new StudentService();

        int studentId = 1;

        SwingUtilities.invokeLater(() -> {
            StudentDashboardFrame dashboard = new StudentDashboardFrame(studentId, studentService);
            dashboard.setVisible(true);
        });
    }
}
