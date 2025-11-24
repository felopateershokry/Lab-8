package skillfrog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.lang.System.Logger.Level;
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

    // Check if the course is completed
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
                for (Object v : courseProgress.values()) {
                    if (!(v instanceof Boolean) || !((Boolean) v)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Generate and store certificate data
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

                List<Map<String, Object>> certs
                        = (user.get("certificates") instanceof List)
                        ? (List<Map<String, Object>>) user.get("certificates")
                        : new ArrayList<>();

                Map<String, Object> certRef = Map.of(
                        "certificateId", certificateId,
                        "courseId", courseId,
                        "issueDate", issueDate
                );

                certs.add(certRef);
                user.put("certificates", certs);

                updated = true;
                break;
            }
        }

        if (!updated) {
            users.add(Map.of(
                    "userId", studentId,
                    "certificates", List.of(
                            Map.of(
                                    "certificateId", certificateId,
                                    "courseId", courseId,
                                    "issueDate", issueDate
                            )
                    )
            ));
        }

        writeUsers(users);
        return certificate;
    }

    public File generatePdfCertificate(Map<String, Object> certificate, String outputDir) throws IOException {

        String certificateId = certificate.get("certificateId").toString();
        String studentId = certificate.get("studentId").toString();
        String courseId = certificate.get("courseId").toString();
        String issueDate = certificate.get("issueDate").toString();

        // create directory
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File out = new File(dir, "certificate_" + certificateId + ".pdf");

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();

                // ======= 1) Draw Elegant Border =======
                cs.setLineWidth(2f);
                cs.addRect(40, 40, pageWidth - 80, pageHeight - 80);
                cs.stroke();

                // ======= 2) Title =======
                String title = "Certificate of Completion";
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 32);
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(title) / 1000 * 32;
                cs.newLineAtOffset((pageWidth - titleWidth) / 2, 680);
                cs.showText(title);
                cs.endText();

                // ======= 3) Separator Line =======
                cs.setLineWidth(1f);
                cs.moveTo(100, 660);
                cs.lineTo(pageWidth - 100, 660);
                cs.stroke();

                // ======= 4) Student Info =======
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 14);
                cs.newLineAtOffset(120, 610);
                cs.showText("This certifies that Student ID: " + studentId);
                cs.endText();

                // ======= 5) Course =======
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.newLineAtOffset(120, 580);
                cs.showText("has successfully completed the course: " + courseId);
                cs.endText();

                // ======= 6) Issue Date =======
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 14);
                cs.newLineAtOffset(120, 550);
                cs.showText("Issued on: " + issueDate);
                cs.endText();

                // ======= 7) Signature Block =======
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 14);
                cs.newLineAtOffset(120, 500);
                cs.showText("Instructor Signature: __________________________");
                cs.endText();

                // ======= 8) Certificate ID footer =======
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                cs.newLineAtOffset(120, 460);
                cs.showText("Certificate ID: " + certificateId);
                cs.endText();
            }

            doc.save(out);
        }

        return out;
    }
}
