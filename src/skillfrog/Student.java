package skillfrog;

import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User {

    private ArrayList<String> enrolledCourses;
    private HashMap<String, HashMap<String, Boolean>> progress; // courseId -> lessonId -> completed
    private HashMap<String, HashMap<String, Integer>> quizScore; // courseId -> lessonId -> score
    private HashMap<String, HashMap<String, Integer>> quizAttempts; // courseId -> lessonId -> attempts

    public Student(int id, String name, String email, String pass) {
        super(id, name, email, pass, "student");
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
        this.quizScore = new HashMap<>();
        this.quizAttempts = new HashMap<>();
    }

    // ===== ENROLLED COURSES =====
    public ArrayList<String> getEnrolledCourses() {
        if (enrolledCourses == null) {
            enrolledCourses = new ArrayList<>();
        }
        return enrolledCourses;
    }

    public void ensureCourseInitialized(String courseId) {
        // Initialize progress
        if (!progress.containsKey(courseId)) {
            progress.put(courseId, new HashMap<>());
        }
        // Initialize quizScore
        if (quizScore == null) {
            quizScore = new HashMap<>();
        }
        if (!quizScore.containsKey(courseId)) {
            quizScore.put(courseId, new HashMap<>());
        }
        // Initialize quizAttempts
        if (quizAttempts == null) {
            quizAttempts = new HashMap<>();
        }
        if (!quizAttempts.containsKey(courseId)) {
            quizAttempts.put(courseId, new HashMap<>());
        }
    }

    public void enrollCourse(String courseId) {
        if (!getEnrolledCourses().contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }

    public void setEnrolledCourses(ArrayList<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    // ===== PROGRESS =====
    public HashMap<String, HashMap<String, Boolean>> getProgress() {
        return progress;
    }

    public void setProgress(HashMap<String, HashMap<String, Boolean>> progress) {
        this.progress = progress;
    }

    public void markLessonCompleted(String courseId, String lessonId) {
        HashMap<String, Boolean> courseProgress = progress.getOrDefault(courseId, new HashMap<>());
        courseProgress.put(lessonId, true);
        progress.put(courseId, courseProgress);
    }

    public boolean isLessonCompleted(String courseId, String lessonId) {
        return progress.getOrDefault(courseId, new HashMap<>()).getOrDefault(lessonId, false);
    }

    // ===== QUIZ SCORE =====
    public HashMap<String, HashMap<String, Integer>> getQuizScore() {
        if (quizScore == null) {
            quizScore = new HashMap<>();
        }
        return quizScore;
    }

    public void setQuizScore(HashMap<String, HashMap<String, Integer>> quizScore) {
        this.quizScore = quizScore;
    }

    public void setQuizScoreForLesson(String courseId, String lessonId, int score) {
        HashMap<String, Integer> courseScores = quizScore.getOrDefault(courseId, new HashMap<>());
        courseScores.put(lessonId, score);
        quizScore.put(courseId, courseScores);
    }

    public int getQuizScoreForLesson(String courseId, String lessonId) {
        return getQuizScore().getOrDefault(courseId, new HashMap<>()).getOrDefault(lessonId, 0);
    }

    // ===== QUIZ ATTEMPTS =====
    public HashMap<String, HashMap<String, Integer>> getQuizAttempts() {
        if (quizAttempts == null) {
            quizAttempts = new HashMap<>();
        }
        return quizAttempts;
    }

    public HashMap<String, Integer> getQuizAttemptsForCourse(String courseId) {
        return getQuizAttempts().getOrDefault(courseId, new HashMap<>());
    }

    public void initializeQuizAttemptsForCourse(String courseId) {
        if (quizAttempts == null) {
            quizAttempts = new HashMap<>();
        }
        if (!quizAttempts.containsKey(courseId)) {
            quizAttempts.put(courseId, new HashMap<>());
        }
    }

    public int getQuizAttempts(String courseId, String lessonId) {
        return getQuizAttemptsForCourse(courseId).getOrDefault(lessonId, 0);
    }

    public void incrementQuizAttempts(String courseId, String lessonId) {
        HashMap<String, Integer> courseAttempts = getQuizAttemptsForCourse(courseId);
        int current = courseAttempts.getOrDefault(lessonId, 0);
        courseAttempts.put(lessonId, current + 1);
        quizAttempts.put(courseId, courseAttempts);
    }

<<<<<<< Updated upstream
    public void recordQuizScore(String courseId, String lessonId, int score) {
        HashMap<String, Integer> scores = quizScore.getOrDefault(courseId, new HashMap<>());
        scores.put(lessonId, score);
        quizScore.put(courseId, scores);
    }

    public void recordLessonCompletion(String courseId, String lessonId) {
        markLessonCompleted(courseId, lessonId);
    }

=======
>>>>>>> Stashed changes
    public int getCompletedLessonsCount(String courseId) {
        if (!progress.containsKey(courseId)) {
            return 0;
        }
        int count = 0;
        for (boolean done : progress.get(courseId).values()) {
            if (done) {
                count++;
            }
        }
        return count;
    }
}
