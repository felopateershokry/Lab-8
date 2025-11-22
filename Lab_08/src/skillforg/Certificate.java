/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillforg;
import java.time.LocalDate;
import java.util.UUID;
/**
 *
 * @author PC
 */
public class Certificate {

    private String certificateId;   // e.g. UUID
    private String studentId;
    private String courseId;
    private String issueDate;       // ISO date string e.g. 2025-11-22
    private String verificationCode; // e.g. short UUID/hash

    public Certificate(String studentId, String courseId) {
        this.certificateId = UUID.randomUUID().toString();
        this.studentId = studentId;
        this.courseId = courseId;
        this.issueDate = LocalDate.now().toString();
        this.verificationCode = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    // full constructor for loading from JSON
    public Certificate(String certificateId, String studentId, String courseId, String issueDate, String verificationCode) {
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.issueDate = issueDate;
        this.verificationCode = verificationCode;
    }

    // getters and setters...
    public String getCertificateId() {
        return certificateId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}
