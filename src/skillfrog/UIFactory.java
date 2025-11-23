package skillfrog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
                    ((StudentDashboardFrame) parentFrame).showAvailableCourses();
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
        lessonPanel.setMaximumSize(new Dimension(600, 70));

        JLabel lessonLabel = new JLabel(lesson.getId() + ": " + lesson.getTitle());
        lessonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lessonPanel.add(lessonLabel, BorderLayout.CENTER);

        boolean lessonCompleted = studentService
                .getLessonProgress(studentId, courseId)
                .getOrDefault(String.valueOf(lesson.getId()), false);

        JProgressBar progressBar = new JProgressBar(0, 1);
        progressBar.setValue(lessonCompleted ? 1 : 0);
        progressBar.setStringPainted(true);
        progressBar.setString(lessonCompleted ? "Completed" : "Pending");
        lessonPanel.add(progressBar, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setOpaque(false);

        Quiz quiz = lesson.getQuiz();
        boolean quizExists = (quiz != null);

        // ✅ Use courseId in hasPassedQuiz
        boolean quizPassedNow = !quizExists && studentService.hasPassedQuiz(studentId, courseId, lesson.getId());

        JButton markComplete = new JButton("Mark Completed");
        markComplete.setBackground(new Color(0xFF9800));
        markComplete.setForeground(Color.WHITE);
        markComplete.setFocusPainted(false);
        markComplete.setFont(new Font("Segoe UI", Font.BOLD, 12));
        markComplete.setEnabled(!lessonCompleted && (!quizExists || quizPassedNow));

        markComplete.addActionListener(e -> {
            boolean stillQuizPassed = !quizExists || studentService.hasPassedQuiz(studentId, courseId, lesson.getId());
            if (!stillQuizPassed) {
                JOptionPane.showMessageDialog(parentFrame,
                        "You must pass this lesson's quiz before marking it complete.");
                return;
            }

            boolean success = studentService.markLessonCompleted(studentId, courseId, lesson.getId());
            if (success) {
                progressBar.setValue(1);
                progressBar.setString("Completed");
                markComplete.setEnabled(false);
                JOptionPane.showMessageDialog(parentFrame, "Lesson marked completed!");
                if (parentFrame instanceof StudentDashboardFrame) {
                    ((StudentDashboardFrame) parentFrame).showLessons(
                            studentService.getEnrolledCourses(studentId)
                                    .stream()
                                    .filter(c -> c.getId() == courseId)
                                    .findFirst()
                                    .orElse(null)
                    );
                }
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Cannot mark lesson.");
            }
        });

        buttonsPanel.add(markComplete);

        if (quizExists) {
            JButton quizBtn = new JButton("Start Quiz");
            quizBtn.setBackground(new Color(0x4CAF50));
            quizBtn.setForeground(Color.WHITE);
            quizBtn.setFocusPainted(false);
            quizBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            quizBtn.setEnabled(!quizPassedNow);

            quizBtn.addActionListener(ae -> {
                QuizFrame quizFrame = new QuizFrame(studentId, lesson.getId(), studentService, courseId);
                quizFrame.setVisible(true);

                quizFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        if (parentFrame instanceof StudentDashboardFrame) {
                            ((StudentDashboardFrame) parentFrame).showLessons(
                                    studentService.getEnrolledCourses(studentId)
                                            .stream()
                                            .filter(c -> c.getId() == courseId)
                                            .findFirst()
                                            .orElse(null)
                            );
                        }
                    }
                });
            });

            buttonsPanel.add(quizBtn);
        }

        lessonPanel.add(buttonsPanel, BorderLayout.EAST);
        return lessonPanel;
    }

}
