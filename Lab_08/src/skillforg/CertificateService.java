/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillforg;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author PC
 */
public class CertificateService {
      private static final String USERS_JSON = "data/users.json"; // adapt path

    public static Certificate issueCertificate(String studentId, String courseId) throws IOException {
        Certificate cert = new Certificate(studentId, courseId);

        // create JSON object for certificate
        JSONObject certJson = new JSONObject();
        certJson.put("certificateId", cert.getCertificateId());
        certJson.put("studentId", cert.getStudentId());
        certJson.put("courseId", cert.getCourseId());
        certJson.put("issueDate", cert.getIssueDate());
        certJson.put("verificationCode", cert.getVerificationCode());

        // store certificate JSON file optionally
        String certFileName = "certificates/" + cert.getCertificateId() + ".json";
        Files.createDirectories(Paths.get("certificates"));
        Files.write(Paths.get(certFileName), certJson.toString(2).getBytes());

        // update users.json: add certificate ref to student's profile
        attachCertificateToUserProfile(cert);

        return cert;
    }

    private static void attachCertificateToUserProfile(Certificate cert) throws IOException {
        Path usersPath = Paths.get(USERS_JSON);
        String text = new String(Files.readAllBytes(usersPath));
        JSONObject usersRoot = new JSONObject(text);

        // assuming users.json has { "users": [ { "id": "...", "name": "...", "certificates": [ ... ] }, ... ] }
        JSONArray usersArray = usersRoot.getJSONArray("users");
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject u = usersArray.getJSONObject(i);
            if (u.getString("id").equals(cert.getStudentId())) {
                JSONArray certs = u.has("certificates") ? u.getJSONArray("certificates") : new JSONArray();
                JSONObject ref = new JSONObject();
                ref.put("certificateId", cert.getCertificateId());
                ref.put("courseId", cert.getCourseId());
                ref.put("issueDate", cert.getIssueDate());
                ref.put("file", "certificates/" + cert.getCertificateId() + ".json");
                ref.put("pdf", "certificates/" + cert.getCertificateId() + ".pdf");
                certs.put(ref);
                u.put("certificates", certs);
                break;
            }
        }

        Files.write(usersPath, usersRoot.toString(2).getBytes());
    }
}
