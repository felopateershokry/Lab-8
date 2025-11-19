/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package skillfrog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Enter Computer
 */
public class StudentDashboardFrame extends javax.swing.JFrame {

    private final JsonDatabaseManager db = new JsonDatabaseManager();
    private final CourseService service = new CourseService(db);
    private StudentService studentService;
    private int studentId;
    private JPanel contentArea;

    public StudentDashboardFrame(int studentId, StudentService studentService) {
        this.studentId = studentId;
        this.studentService = studentService;

        initUI();
        showAvailableCourses();
    }

    StudentDashboardFrame() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void initUI() {
        setTitle("Student Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0xf5f5f5));
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(new Color(0x2196F3));
        JLabel welcomeLabel = new JLabel("Welcome, Student!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topBar.add(welcomeLabel);
        mainPanel.add(topBar, BorderLayout.NORTH);

        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(new Color(0xe0e0e0));
        sideBar.setPreferredSize(new Dimension(150, 0));

        JButton btnAvailable = new JButton("Available Courses");
        btnAvailable.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAvailable.addActionListener(e -> showAvailableCourses());

        JButton btnEnrolled = new JButton("Enrolled Courses");
        btnEnrolled.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEnrolled.addActionListener(e -> showEnrolledCourses());

        sideBar.add(Box.createVerticalStrut(20));
        sideBar.add(btnAvailable);
        sideBar.add(Box.createVerticalStrut(10));
        sideBar.add(btnEnrolled);

        mainPanel.add(sideBar, BorderLayout.WEST);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(new Color(0xf5f5f5));
        mainPanel.add(contentArea, BorderLayout.CENTER);
    }

    public void showAvailableCourses() {
        List<Course> available = studentService.getAvailableCourses(studentId);
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(0xf5f5f5));

        for (Course c : available) {
            JPanel coursePanel = UIFactory.createCoursePanel(
                    c, true, studentId, studentService, this
            );
            container.add(coursePanel);
            container.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);

        contentArea.removeAll();
        contentArea.add(scrollPane, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    public void showEnrolledCourses() {
        List<Course> enrolled = studentService.getEnrolledCourses(studentId);
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(0xf5f5f5));

        for (Course c : enrolled) {
            JPanel coursePanel = UIFactory.createCoursePanel(
                    c, false, studentId, studentService, this
            );
            container.add(coursePanel);
            container.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);

        contentArea.removeAll();
        contentArea.add(scrollPane, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    public void showLessons(Course course) {
        List<Lesson> lessons = course.getLessons();
        if (lessons == null || lessons.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No lessons added yet!");
            return;
        }

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(0xf5f5f5));

        for (Lesson l : lessons) {
            JPanel lessonPanel = UIFactory.createLessonPanel(
                    l, studentId, studentService, this, course.getId()
            );
            container.add(lessonPanel);
            container.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);

        contentArea.removeAll();
        contentArea.add(scrollPane, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(189, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StudentDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StudentDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StudentDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StudentDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new StudentDashboardFrame().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
