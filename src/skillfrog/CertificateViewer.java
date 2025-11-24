/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

/**
 *
 * @author Lenovo
 */
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CertificateViewer {

    public static void showCertificate(Map<String, Object> certificate, JsonDatabaseManager db, CourseService service) {

        JFrame frame = new JFrame("Certificate Viewer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        int studentId = Integer.parseInt(certificate.get("studentId").toString());
        int courseId = Integer.parseInt(certificate.get("courseId").toString());

        String studentName = db.getUserById(studentId).getUsername();
        String courseTitle = service.getCourse(courseId).getTitle();

        StringBuilder sb = new StringBuilder();
        sb.append("Certificate ID: ").append(certificate.get("certificateId")).append("\n");
        sb.append("Student: ").append(studentName).append("\n");
        sb.append("Course: ").append(courseTitle).append("\n");
        sb.append("Issue Date: ").append(certificate.get("issueDate")).append("\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton downloadBtn = new JButton("Download as PDF");
        downloadBtn.addActionListener(e -> {
            try {
                CertificateManager cm = new CertificateManager("users.json");
                File pdf = cm.generatePdfCertificate(certificate, "certificates");

                JOptionPane.showMessageDialog(frame,
                        "PDF Saved at:\n" + pdf.getAbsolutePath(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Error generating PDF!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.add(downloadBtn, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

}
