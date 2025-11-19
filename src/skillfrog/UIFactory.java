package skillfrog;

import javax.swing.*;
import java.awt.*;

public class UIFactory {

    public static JPanel createCoursePanel(
            Course course,
            boolean showEnrollButton,
            int studentId,
            StudentService studentService,
            JFrame parentFrame
    ) {
        JPanel coursePanel = new JPanel(new BorderLayout(10, 10));
        coursePanel.setBackground(Color.WHITE);
        coursePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        coursePanel.setMaximumSize(new Dimension(600, 80));

        JLabel title = new JLabel(course.getTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        coursePanel.add(title, BorderLayout.CENTER);

        JButton actionBtn = new JButton();
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setFocusPainted(false);
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        if (showEnrollButton) {
            actionBtn.setText("Enroll");
            actionBtn.setBackground(new Color(0x4CAF50));
            actionBtn.addActionListener(e -> {
                boolean success = studentService.enroll(studentId, course.getId());
                if (success) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Enrolled in " + course.getTitle());
                    parentFrame.repaint();
                } else {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Already enrolled or error");
                }
            });
        } else {
            actionBtn.setText("View Lessons");
            actionBtn.setBackground(new Color(0x2196F3));
            actionBtn.addActionListener(e -> {
                ((StudentDashboardFrame) parentFrame).showLessons(course);
            });
        }

        coursePanel.add(actionBtn, BorderLayout.EAST);

        return coursePanel;
    }

    public static JPanel createLessonPanel(
            Lesson lesson,
            int studentId,
            StudentService studentService,
            JFrame parentFrame,
            int courseId
    ) {
        JPanel lessonPanel = new JPanel(new BorderLayout(10, 10));
        lessonPanel.setBackground(Color.WHITE);
        lessonPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lessonPanel.setMaximumSize(new Dimension(600, 60));

        JLabel lessonLabel = new JLabel(lesson.getId() + ": " + lesson.getTitle());
        lessonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lessonPanel.add(lessonLabel, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar(0, 1);
        progressBar.setValue(studentService.getLessonProgress(studentId, courseId)
                .getOrDefault(String.valueOf(lesson.getId()), false) ? 1 : 0);
        progressBar.setStringPainted(true);
        progressBar.setString(progressBar.getValue() == 1 ? "Completed" : "Pending");
        lessonPanel.add(progressBar, BorderLayout.WEST);

        JButton markComplete = new JButton("Mark Completed");
        markComplete.setBackground(new Color(0xFF9800));
        markComplete.setForeground(Color.WHITE);
        markComplete.setFocusPainted(false);
        markComplete.setFont(new Font("Segoe UI", Font.BOLD, 12));

        markComplete.addActionListener(e -> {
            boolean success = studentService.markLessonCompleted(studentId, courseId, lesson.getId());
            if (success) {
                progressBar.setValue(1);
                progressBar.setString("Completed");
                JOptionPane.showMessageDialog(parentFrame, "Lesson marked completed!");
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Cannot mark lesson.");
            }
        });

        lessonPanel.add(markComplete, BorderLayout.EAST);

        return lessonPanel;
    }
}
