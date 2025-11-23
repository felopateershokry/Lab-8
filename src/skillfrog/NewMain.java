/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package skillfrog;

/**
 *
 * @author GAMING
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        AnalyticsService analytics = new AnalyticsService();

        int courseId = 103;

        // 1) متوسط درجات كل درس
        System.out.println("---- Quiz Averages ----");
        analytics.getLessonQuizAverages(courseId).forEach((lessonId, avg) -> {
            System.out.println("Lesson " + lessonId + " -> Average Score = " + avg);
        });

        // 2) نسبة إكمال الكورس لكل طالب
        System.out.println("\n---- Course Completion Percentages ----");
        analytics.getCourseCompletionPercentages(courseId).forEach((userId, percent) -> {
            System.out.println("Student " + userId + " -> " + percent + "%");
        });

        // 3) نسبة إكمال كل درس
        System.out.println("\n---- Lesson Completion Rates ----");
        analytics.getLessonCompletionRates(courseId).forEach((lessonId, percent) -> {
            System.out.println("Lesson " + lessonId + " -> " + percent + "%");
        });

        // 4) تقرير شامل
        System.out.println("\n==== FULL ANALYTICS ====");
        CourseAnalyticsResult result = analytics.getCourseAnalytics(courseId);

        System.out.println("Course ID: " + result.getCourseId());
        System.out.println("Quiz Averages: " + result.getQuizAverages());
        System.out.println("Student Completion: " + result.getStudentCompletion());
        System.out.println("Lesson Completion: " + result.getLessonCompletionRates());
    }
}
