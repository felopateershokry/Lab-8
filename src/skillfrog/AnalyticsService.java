/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author GAMING
 */
public class AnalyticsService {

    private JsonDatabaseManager db;

    public AnalyticsService() {
        db = new JsonDatabaseManager();
    }

    public Map<Integer, Double> getLessonQuizAverages(int courseId) {
        Map<Integer, Double> averages = new HashMap<>();

        Course course = db.loadCourses().stream()
                .filter(c -> c.getId() == courseId)
                .findFirst()
                .orElse(null);

        if (course == null) {
            return averages;
        }

        List<Lesson> lessons = course.getLessons();
        List<Student> students = db.getAllStudentsForCourse(courseId);

        for (Lesson lesson : lessons) {
            int lessonId = lesson.getId();
            int total = 0, count = 0;

            for (Student s : students) {
                int score = s.getQuizScoreForLesson(
                        String.valueOf(courseId),
                        String.valueOf(lessonId)
                );

                if (score > 0) {
                    total += score;
                    count++;
                }
            }

            double avg = count == 0 ? 0 : (total * 1.0) / count;
            averages.put(lessonId, avg);
        }

        return averages;
    }

    public Map<Integer, Double> getCourseCompletionPercentages(int courseId) {
        Map<Integer, Double> data = new HashMap<>();

        Course course = db.loadCourses().stream()
                .filter(c -> c.getId() == courseId)
                .findFirst()
                .orElse(null);

        if (course == null) {
            return data;
        }

        int totalLessons = course.getLessons().size();
        List<Student> students = db.getAllStudentsForCourse(courseId);

        for (Student s : students) {
            int completed = s.getCompletedLessonsCount(String.valueOf(courseId));
            double percentage = totalLessons == 0 ? 0 : (completed * 100.0 / totalLessons);
            data.put(s.getUserId(), percentage);
        }

        return data;
    }

    public Map<Integer, Double> getLessonCompletionRates(int courseId) {
        Map<Integer, Double> data = new HashMap<>();

        Course course = db.loadCourses().stream()
                .filter(c -> c.getId() == courseId)
                .findFirst()
                .orElse(null);

        if (course == null) {
            return data;
        }

        List<Student> students = db.getAllStudentsForCourse(courseId);

        for (Lesson lesson : course.getLessons()) {
            int lessonId = lesson.getId();
            int completed = 0;
            for (Student s : students) {
                if (s.isLessonCompleted(String.valueOf(courseId), String.valueOf(lessonId))) {
                    completed++;
                }
            }
            double percentage = students.size() == 0 ? 0 : (completed * 100.0 / students.size());
            data.put(lessonId, percentage);
        }

        return data;
    }

    public CourseAnalyticsResult getCourseAnalytics(int courseId) {
        Map<Integer, Double> quizAvg = getLessonQuizAverages(courseId);
        Map<Integer, Double> studentComp = getCourseCompletionPercentages(courseId);
        Map<Integer, Double> lessonComp = getLessonCompletionRates(courseId);

        return new CourseAnalyticsResult(courseId, quizAvg, studentComp, lessonComp);
    }
}
