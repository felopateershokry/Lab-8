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
import java.util.Map;

public class CertificateViewer {

    public static void showCertificate(Map<String, Object> certificate) {

        JFrame frame = new JFrame("Certificate Viewer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        StringBuilder sb = new StringBuilder();
        sb.append("Certificate ID: ").append(certificate.get("certificateId")).append("\n");
        sb.append("Student ID: ").append(certificate.get("studentId")).append("\n");
        sb.append("Course ID: ").append(certificate.get("courseId")).append("\n");
        sb.append("Issue Date: ").append(certificate.get("issueDate")).append("\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.getContentPane().add(scrollPane);
        frame.setVisible(true);
    }
}
