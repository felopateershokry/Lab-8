/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillforg;
import java.util.List;
import java.util.Map;
import skillforg.Lesson;
/**
 *
 * @author PC
 */
public class CourseCompletionChecker {
     public static boolean isCourseCompleted(String studentId, Course course, StudentProgress studentProgress) {
        List<Lesson> lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            // 1) check lesson visited/completed (depending on your data model)
            if (!studentProgress.isLessonCompleted(lesson.getId())) {
                return false;
            }
            // 2) check quiz pass
            QuizResult qres = studentProgress.getQuizResultForLesson(lesson.getId());
            if (qres == null || !qres.isPassed()) {
                return false;
            }
        }
        return true;
    }
}
