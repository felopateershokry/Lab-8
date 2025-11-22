package skillforg;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import java.io.IOException;
import java.nio.file.*;
/**
 *
 * @author PC
 */
public class PdfCertificateGenerator {
     public static Path createPdf(Certificate cert, String studentName, String courseTitle) throws IOException {
        Files.createDirectories(Paths.get("certificates"));
        String out = "certificates/" + cert.getCertificateId() + ".pdf";
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            PDPageContentStream cs = new PDPageContentStream(doc, page);

            // title
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 26);
            cs.newLineAtOffset(90, 650);
            cs.showText("Certificate of Completion");
            cs.endText();

            // student name
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 20);
            cs.newLineAtOffset(90, 580);
            cs.showText("This certifies that:");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 22);
            cs.newLineAtOffset(90, 540);
            cs.showText(studentName);
            cs.endText();

            // course
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 16);
            cs.newLineAtOffset(90, 500);
            cs.showText("has successfully completed the course:");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
            cs.newLineAtOffset(90, 470);
            cs.showText(courseTitle);
            cs.endText();

            // details - date and verification code
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(90, 420);
            cs.showText("Issue Date: " + cert.getIssueDate());
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(90, 400);
            cs.showText("Certificate ID: " + cert.getCertificateId());
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(90, 380);
            cs.showText("Verification Code: " + cert.getVerificationCode());
            cs.endText();

            cs.close();
            doc.save(out);
        }

        return Paths.get(out);
    }
}
