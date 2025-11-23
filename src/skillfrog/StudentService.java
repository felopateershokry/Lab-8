/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.stream.Collectors;

/**
 *
 * @author Enter Computer
 */
public class StudentService {

    private JsonDatabaseManager dbManager;

    public StudentService() {
        dbManager = new JsonDatabaseManager();
    }

    public List<Course> getAvailableCourses(int studentID) {

        List<User> users = dbManager.getUsers();
        Student target = users.stream()
                .filter(u -> u.getUserId() == studentID && u instanceof Student)
                .map(u -> (Student) u)
                .findFirst()
                .orElse(null);

        if (target == null) {
            return new ArrayList<>();
        }

        List<Course> allCourses = new JsonDatabaseManager().loadCourses();

        return allCourses.stream()
                .filter(c -> c.getApprovalStatus() != null
                && c.getApprovalStatus().equalsIgnoreCase("APPROVED"))
                .filter(c -> target.getEnrolledCourses() == null
                || !target.getEnrolledCourses().contains(c.getId()))
                .collect(Collectors.toList());
    }

    public List<Course> getEnrolledCourses(int studentID) {
        List<User> users = dbManager.getUsers();
        Student target = null;

        for (User i : users) {
            if (i.getUserId() == studentID && i instanceof Student) {
                target = (Student) i;
                break;
            }
        }

        if (target == null) {
            System.out.println("student not found");
            return new ArrayList<>();
        }

        List<Course> allCourses = new JsonDatabaseManager().loadCourses();

        List<Course> enrolled = new ArrayList<>();

        for (Course i : allCourses) {
            if (target.getEnrolledCourses().contains(String.valueOf(i.getId()))) {
                enrolled.add(i);
            }
        }

        return enrolled;

    }

    public boolean enroll(int studentID, int courseID) {
        List<User> users = dbManager.getUsers();
        Student target = null;

        for (User i : users) {
            if (i.getUserId() == studentID && i instanceof Student) {
                target = (Student) i;
                break;
            }
        }

        if (target == null) {
            System.out.println("student not found");
            return false;
        }

        CourseService enroll = new CourseService(dbManager);
        Course course = enroll.getCourse(courseID);

        if (course == null) {
            System.out.println("course not found");
            return false;
        }

        if (target.getEnrolledCourses().contains(String.valueOf(courseID))) {
            System.out.println("Student already enrolled");
            return false;
        }

        target.getEnrolledCourses().add(String.valueOf(courseID));

        course.getEnrolledStudents().add(String.valueOf(studentID));
        

        JsonDatabaseManager repo = new JsonDatabaseManager();
        List<Course> allCourses = repo.loadCourses();

        for (int i = 0; i < allCourses.size(); i++) {
            if (allCourses.get(i).getId() == courseID) {
                allCourses.set(i, course);
                break;
            }
        }

        repo.saveCourses(allCourses);
        dbManager.saveUsers();

        return true;

    }

    public List<Lesson> getLessons(int studentID, int courseID) {
    List<Course> enrolled = getEnrolledCourses(studentID);

    for (Course i : enrolled) {
        if (i.getId() == courseID) {
            
            List<Lesson> lessons = i.getLessons();
            if (lessons == null) {
                System.out.println("no lessons added yet");
                return new ArrayList<>();
            }

            List<Lesson> accessibleLessons = new ArrayList<>();
            for (int idx = 0; idx < lessons.size(); idx++) {
                Lesson lesson = lessons.get(idx);

                if (idx == 0) {
                    accessibleLessons.add(lesson);
                    continue;
                }

                int prevLessonId = lessons.get(idx - 1).getId();
                boolean prevCompleted = getLessonProgress(studentID, courseID)
                                        .getOrDefault(String.valueOf(prevLessonId), false);

                if (prevCompleted) {
                    accessibleLessons.add(lesson);
                } else {
                    System.out.println("Complete previous lesson first to access lesson " + lesson.getId());
                    break; // stop showing further lessons
                }
            }

            return accessibleLessons;
        }
    }

    System.out.println("Course not found or student not enrolled");
    return new ArrayList<>();
}

    public boolean markLessonCompleted(int studentID, int courseID, int lessonID) {
        List<User> users = dbManager.getUsers();
        Student target = null;

        for (User i : users) {
            if (i.getUserId() == studentID && i instanceof Student) {
                target = (Student) i;
                break;
            }
        }

        if (target == null) {
            System.out.println("Student not found");
            return false;
        }

        List<Course> allCourses = new JsonDatabaseManager().loadCourses();
        Course targetCourse = null;

        for (Course i : allCourses) {
            if (i.getId() == courseID) {
                targetCourse = i;
                break;
            }
        }

        if (targetCourse == null) {
            System.out.println("Course not found");
            return false;
        }

        if (!target.getEnrolledCourses().contains(String.valueOf(courseID))) {
            System.out.println("Student not enrolled in this course");
            return false;
        }

        HashMap<String, Boolean> courseProgress = target.getProgress().getOrDefault(
                String.valueOf(courseID),
                new HashMap<String, Boolean>()
        );

        courseProgress.put(String.valueOf(lessonID), true);

        target.getProgress().put(String.valueOf(courseID), courseProgress);

        dbManager.saveUsers();

        return true;
    }

    public HashMap<String, Boolean> getLessonProgress(int studentID, int courseID) {
        List<User> users = dbManager.getUsers();
        Student target = null;

        for (User i : users) {
            if (i.getUserId() == studentID && i instanceof Student) {
                target = (Student) i;
                break;
            }
        }

        if (target == null) {
            System.out.println("Student not found");
            return new HashMap<>();
        }
        
        return target.getProgress().getOrDefault(
                String.valueOf(courseID),
                new HashMap<String, Boolean>()
        );
    }
    
    
    public Quiz getQuiz(int lessonID){
        for(Course course: dbManager.loadCourses()){
            for(Lesson lesson: course.getLessons()){
                if(lesson.getId()==lessonID)
                    return lesson.getQuiz(); 
            }
        }
        return null;
    }
    
    
   public boolean submitQuiz(int studentID, int courseId, int lessonID, List<Integer> answers) {
    // 1️⃣ Get student
    Student student = getStudentById(studentID);
    if (student == null) {
        System.out.println("Student not found");
        return false;
    }

    String courseIdStr = String.valueOf(courseId);

    // 2️⃣ Ensure nested maps for this course exist
    student.ensureCourseInitialized(courseIdStr);

    // 3️⃣ Find the lesson in the specified course
    Course course = dbManager.loadCourses().stream()
            .filter(c -> c.getId() == courseId)
            .findFirst()
            .orElse(null);

    if (course == null) {
        System.out.println("Course not found");
        return false;
    }

    Lesson lesson = course.getLessons().stream()
            .filter(l -> l.getId() == lessonID)
            .findFirst()
            .orElse(null);

    if (lesson == null) {
        System.out.println("Lesson not found in course");
        return false;
    }

    // 4️⃣ Get the quiz
    Quiz quiz = lesson.getQuiz();
    if (quiz == null) {
        System.out.println("Quiz not found");
        return false;
    }

    // 5️⃣ Check retry rules
    int attempts = student.getQuizAttempts(courseIdStr, String.valueOf(lessonID));
    if (!quiz.isRetryAllowed() && attempts >= 1) {
        return false;
    }

    // 6️⃣ Calculate score
    List<Integer> correct = quiz.getCorrectAnswers();
    int totalQuestions = correct.size();
    int score = 0;
    for (int i = 0; i < totalQuestions; i++) {
        if (answers.get(i).equals(correct.get(i))) {
            score++;
        }
    }

    int percentage = (int)((score * 100.0) / totalQuestions);
    boolean passed = percentage >= 60;

    // 7️⃣ Update attempts and score
    student.incrementQuizAttempts(courseIdStr, String.valueOf(lessonID));
    student.setQuizScoreForLesson(courseIdStr, String.valueOf(lessonID), percentage);

    // 8️⃣ Mark lesson completed if passed
    if (passed) {
        student.markLessonCompleted(courseIdStr, String.valueOf(lessonID));
    }

    // 9️⃣ Save users
    dbManager.saveUsers();
    return passed;
}

public boolean hasPassedQuiz(int studentID, int courseId, int lessonID) {
    Student student = getStudentById(studentID);
    if (student == null) return false;
    Quiz quiz = getQuiz(lessonID);
    if (quiz == null) {
        return true;
    }
    String courseIdStr = String.valueOf(courseId);
    student.ensureCourseInitialized(courseIdStr);

    int score = student.getQuizScoreForLesson(courseIdStr, String.valueOf(lessonID));
    return score >= 60;
}

    
    private Student getStudentById(int studentId) {
    return dbManager.getUsers().stream()
            .filter(u -> u instanceof Student && u.getUserId() == studentId)
            .map(u -> (Student) u)
            .findFirst()
            .orElse(null);
}
    
    public void addStudent(Student s) {
        dbManager.addUser(s);
    }
}


