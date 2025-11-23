package skillfrog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class CertificateManager {

    private final File usersFile;
    private final ObjectMapper mapper;

    public CertificateManager(String usersJsonPath) {
        this.usersFile = new File(usersJsonPath);
        this.mapper = new ObjectMapper();
    }

    private List<Map<String, Object>> readUsers() throws IOException {
        if (!usersFile.exists()) {
            return new ArrayList<>();
        }
        return mapper.readValue(usersFile, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    private void writeUsers(List<Map<String, Object>> users) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(usersFile, users);
    }

    public boolean isCourseCompleted(int studentId, String courseId) throws IOException {
        List<Map<String, Object>> users = readUsers();

        for (Map<String, Object> user : users) {
            Object uidObj = user.get("userId");
            int uid = uidObj instanceof Number ? ((Number) uidObj).intValue() : Integer.parseInt(uidObj.toString());

            if (uid == studentId) {
                Object progressObj = user.get("progress");
                if (!(progressObj instanceof Map)) {
                    return false;
                }

                Map<?, ?> progress = (Map<?, ?>) progressObj;
                Object courseProgressObj = progress.get(courseId);
                if (!(courseProgressObj instanceof Map)) {
                    return false;
                }

                Map<?, ?> courseProgress = (Map<?, ?>) courseProgressObj;
                for (Object value : courseProgress.values()) {
                    if (!(value instanceof Boolean)) {
                        return false;
                    }
                    if (!((Boolean) value)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> generateAndStoreCertificate(int studentId, String courseId) throws IOException {
        String certificateId = UUID.randomUUID().toString();
        String issueDate = LocalDate.now().toString();

        Map<String, Object> certificate = new LinkedHashMap<>();
        certificate.put("certificateId", certificateId);
        certificate.put("studentId", studentId);
        certificate.put("courseId", courseId);
        certificate.put("issueDate", issueDate);

        List<Map<String, Object>> users = readUsers();
        boolean updated = false;

        for (Map<String, Object> user : users) {
            Number uid = (Number) user.get("userId");
            if (uid != null && uid.intValue() == studentId) {
                Object certsObj = user.get("certificates");
                List<Map<String, Object>> certs;
                if (certsObj instanceof List) {
                    certs = (List<Map<String, Object>>) certsObj;
                } else {
                    certs = new ArrayList<>();
                    user.put("certificates", certs);
                }

                Map<String, Object> certRef = new LinkedHashMap<>();
                certRef.put("certificateId", certificateId);
                certRef.put("courseId", courseId);
                certRef.put("issueDate", issueDate);
                certs.add(certRef);
                updated = true;
                break;
            }
        }

        if (!updated) {
            Map<String, Object> newUser = new LinkedHashMap<>();
            newUser.put("userId", studentId);
            newUser.put("certificates", Arrays.asList(
                    new LinkedHashMap<String, Object>() {
                {
                    put("certificateId", certificateId);
                    put("courseId", courseId);
                    put("issueDate", issueDate);
                }
            }
            ));
            users.add(newUser);
        }

        writeUsers(users);
        return certificate;
    }

    public void writeCertificateToFile(Map<String, Object> cert, String path) {
        File file = new File(path);
        file.getParentFile().mkdirs();

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, cert);
            System.out.println("Certificate written to " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
