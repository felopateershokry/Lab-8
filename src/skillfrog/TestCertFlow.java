/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

import java.util.Map;

/**
 *
 * @author Lenovo
 */
public class TestCertFlow {

    public static void main(String[] args) throws Exception {
        CertificateManager cm = new CertificateManager("users.json");
        int studentId = 15;
        String courseId = "103";

        boolean completed = cm.isCourseCompleted(studentId, courseId);
        System.out.println("Completed? " + completed);

        if (completed) {
            Map<String, Object> cert = cm.generateAndStoreCertificate(studentId, courseId);
            System.out.println("Generated certificate: " + cert);
            cm.writeCertificateToFile(cert, "data/output/certificate-" + cert.get("certificateId") + ".json");
            System.out.println("Certificate written to data/output/");
        } else {
            System.out.println("Not completed yet.");
        }
    }
}
