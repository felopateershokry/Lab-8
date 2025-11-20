package skillfrog;

import java.util.*;

public class StudentServiceTest {

    public static void main(String[] args) {

        JsonDatabaseManager dbManager = new JsonDatabaseManager();

        // Create courses
        Course javaCourse = new Course(101, "Java Basics", "Intro to Java");
        Course pythonCourse = new Course(102, "Python Basics", "Intro to Python");
        javaCourse.setApprovalStatus("approved");
        pythonCourse.setApprovalStatus("approved");

        // Create lessons
        Lesson javaLesson1 = new Lesson(1, "Intro", "Java intro content");
        Lesson javaLesson2 = new Lesson(2, "Variables", "Java variables content");
        Lesson pythonLesson1 = new Lesson(1, "Intro", "Python intro content");
        Lesson pythonLesson2 = new Lesson(2, "Data Types", "Python data types content");

        // Create quizzes
        Quiz javaQuiz1 = new Quiz(
                Arrays.asList("Q1", "Q2"),
                Arrays.asList(Arrays.asList("A","B","C"), Arrays.asList("X","Y","Z")),
                Arrays.asList(0, 2),
                true
        );
        Quiz javaQuiz2 = new Quiz(
                Arrays.asList("Q3", "Q4"),
                Arrays.asList(Arrays.asList("1","2","3"), Arrays.asList("4","5","6")),
                Arrays.asList(1, 0),
                true
        );
        Quiz pythonQuiz1 = new Quiz(
                Arrays.asList("Q1", "Q2"),
                Arrays.asList(Arrays.asList("A","B","C"), Arrays.asList("X","Y","Z")),
                Arrays.asList(2, 1),
                false
        );
        Quiz pythonQuiz2 = new Quiz(
                Arrays.asList("Q3", "Q4"),
                Arrays.asList(Arrays.asList("7","8","9"), Arrays.asList("10","11","12")),
                Arrays.asList(1, 0),
                false
        );

        // Attach quizzes to lessons
        javaLesson1.setQuiz(javaQuiz1);
        javaLesson2.setQuiz(javaQuiz2);
        pythonLesson1.setQuiz(pythonQuiz1);
        pythonLesson2.setQuiz(pythonQuiz2);

        // Add lessons to courses
        javaCourse.getLessons().add(javaLesson1);
        javaCourse.getLessons().add(javaLesson2);
        pythonCourse.getLessons().add(pythonLesson1);
        pythonCourse.getLessons().add(pythonLesson2);

        // Add courses to DB
        dbManager.addCourse(javaCourse);
        dbManager.addCourse(pythonCourse);

        // Create students
        Student alice = new Student(1, "Alice", "alice@example.com", "pass123");
        Student bob = new Student(2, "Bob", "bob@example.com", "pass456");

        dbManager.addUser(alice);
        dbManager.addUser(bob);

        // Create student service
        StudentService studentService = new StudentService();

        // --- Available courses ---
        System.out.println("=== Available courses for Alice ===");
        for (Course c : studentService.getAvailableCourses(1)) {
            System.out.println(c.getId() + ": " + c.getTitle());
        }

        // --- Enroll students ---
        System.out.println("\nEnroll Alice in Java Basics:");
        System.out.println("Enrollment success: " + studentService.enroll(1, 101));
        System.out.println("Enroll Alice again: " + studentService.enroll(1, 101));

        System.out.println("\nEnroll Bob in Python Basics:");
        System.out.println("Enrollment success: " + studentService.enroll(2, 102));

        // --- Enrolled courses ---
        System.out.println("\nAlice enrolled courses:");
        for (Course c : studentService.getEnrolledCourses(1)) {
            System.out.println(c.getId() + ": " + c.getTitle());
        }

        System.out.println("\nBob enrolled courses:");
        for (Course c : studentService.getEnrolledCourses(2)) {
            System.out.println(c.getId() + ": " + c.getTitle());
        }

        // --- Attempt quizzes ---
        System.out.println("\nAlice attempts Java Lesson 1 quiz:");
        List<Integer> aliceAnswers1 = Arrays.asList(0, 2); // correct
        boolean passed1 = studentService.submitQuiz(1, 1, aliceAnswers1);
        System.out.println("Quiz passed? " + passed1);

        System.out.println("\nAlice attempts Java Lesson 2 quiz without completing prerequisites:");
        List<Integer> aliceAnswers2 = Arrays.asList(1, 0);
        boolean passed2 = studentService.submitQuiz(1, 2, aliceAnswers2);
        System.out.println("Quiz passed? " + passed2);

        System.out.println("\nBob attempts Python Lesson 1 quiz:");
        List<Integer> bobAnswers1 = Arrays.asList(2, 1);
        boolean bobPassed1 = studentService.submitQuiz(2, 1, bobAnswers1);
        System.out.println("Quiz passed? " + bobPassed1);

        // --- Check progress ---
        System.out.println("\nLesson progress for course 101:");
        HashMap<String, Boolean> progressAlice = studentService.getLessonProgress(1, 101);
        for (String lessonId : progressAlice.keySet()) {
            System.out.println("Lesson " + lessonId + " completed? " + progressAlice.get(lessonId));
        }

        System.out.println("\nLesson progress for course 102:");
        HashMap<String, Boolean> progressBob = studentService.getLessonProgress(2, 102);
        for (String lessonId : progressBob.keySet()) {
            System.out.println("Lesson " + lessonId + " completed? " + progressBob.get(lessonId));
        }

        // --- Edge cases ---
        System.out.println("\nEnroll non-existent student: " + studentService.enroll(99, 101));
        System.out.println("Enroll in non-existent course: " + studentService.enroll(1, 999));
        System.out.println("Alice marks lesson in course 102 (not enrolled): " + studentService.markLessonCompleted(1, 102, 1));

        System.out.println("\nTest completed.");
    }
}
