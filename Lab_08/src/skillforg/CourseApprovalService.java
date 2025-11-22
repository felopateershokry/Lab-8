/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillforg;

import java.util.List;

/**
 *
 * @author Lenovo
 */
public class CourseApprovalService {

    private CourseService service;
    private JsonDatabaseManager repo;

    public CourseApprovalService(CourseService service) {
        this.repo = repo;
    }

    public void approveCourse(int courseId) {
        Course c = service.getCourse(courseId);
        if (c != null) {
            c.setApprovalStatus("approved");
            List<Course> allCourses = repo.loadCourses();

            for (int i = 0; i < allCourses.size(); i++) {
                if (allCourses.get(i).getId() == courseId) {
                    allCourses.set(i, c);
                    break;
                }
            }
            repo.saveCourses(allCourses);
        }
    }

    public void rejectCourse(int courseId) {
        Course c = service.getCourse(courseId);
        if (c != null) {
            c.setApprovalStatus("rejected");
            List<Course> allCourses = repo.loadCourses();

            for (int i = 0; i < allCourses.size(); i++) {
                if (allCourses.get(i).getId() == courseId) {
                    allCourses.set(i, c);
                    break;
                }
            }
            repo.saveCourses(allCourses);
        }
    }

}
