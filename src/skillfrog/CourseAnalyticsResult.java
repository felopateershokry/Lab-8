/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

import java.util.Map;

/**
 *
 * @author GAMING
 */
public class CourseAnalyticsResult {

    private int courseId;
    private Map<Integer, Double> quizAverages;         // lessonId -> avg score
    private Map<Integer, Double> studentCompletion;    // studentId -> completion %
    private Map<Integer, Double> lessonCompletionRates;// lessonId -> completion %

    public CourseAnalyticsResult(int courseId,
            Map<Integer, Double> quizAverages,
            Map<Integer, Double> studentCompletion,
            Map<Integer, Double> lessonCompletionRates) {
        this.courseId = courseId;
        this.quizAverages = quizAverages;
        this.studentCompletion = studentCompletion;
        this.lessonCompletionRates = lessonCompletionRates;
    }

    public int getCourseId() {
        return courseId;
    }

    public Map<Integer, Double> getQuizAverages() {
        return quizAverages;
    }

    public Map<Integer, Double> getStudentCompletion() {
        return studentCompletion;
    }

    public Map<Integer, Double> getLessonCompletionRates() {
        return lessonCompletionRates;
    }
}
