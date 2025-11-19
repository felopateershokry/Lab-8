package skillfrog;

import java.util.ArrayList;
import java.util.List;

public class CourseService {

    private JsonDatabaseManager repo;
    private List<Course> courses;

    public CourseService(JsonDatabaseManager repo) {
        this.repo = repo;
        this.courses = repo.loadCourses();
    }

    CourseService() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean createCourse(Course c, Instructor instructor) {

        if (courseExists(c.getId())) {
            return false;
        }

        courses.add(c);
        repo.saveCourses(courses);

        Instructor real = (Instructor) repo.getUserByEmail(instructor.getEmail());
        real.getCreatedCourses().add(c.getId());
        repo.saveUsers();

        return true;
    }

    public boolean updateCourse(int id, Course newData, Instructor instructor) {

        if (!courseExists(id) || !ownsCourse(id, instructor)) {
            return false;
        }

        for (Course c : courses) {
            if (c.getId() == id) {
                c.setTitle(newData.getTitle());
                c.setDescription(newData.getDescription());
                repo.saveCourses(courses);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCourse(int id, Instructor instructor) {

        if (!courseExists(id) || !ownsCourse(id, instructor)) {
            return false;
        }

        courses.removeIf(c -> c.getId() == id);
        repo.saveCourses(courses);

        Instructor real = (Instructor) repo.getUserByEmail(instructor.getEmail());
        real.getCreatedCourses().remove(Integer.valueOf(id));
        repo.saveUsers();

        return true;
    }

    public boolean addLesson(int courseId, Lesson lesson, Instructor instructor) {

        if (!courseExists(courseId) || !ownsCourse(courseId, instructor)) {
            return false;
        }
        if (lessonExists(courseId, lesson.getId())) {
            return false;
        }

        for (Course c : courses) {
            if (c.getId() == courseId) {
                c.getLessons().add(lesson);
                repo.saveCourses(courses);
                return true;
            }
        }
        return false;
    }

    public boolean editLesson(int courseId, int lessonId, Lesson newData, Instructor instructor) {

        if (!courseExists(courseId) || !ownsCourse(courseId, instructor) || !lessonExists(courseId, lessonId)) {
            return false;
        }

        for (Course c : courses) {
            if (c.getId() == courseId) {
                for (Lesson l : c.getLessons()) {
                    if (l.getId() == lessonId) {
                        l.setTitle(newData.getTitle());
                        l.setContent(newData.getContent());
                        repo.saveCourses(courses);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean deleteLesson(int courseId, int lessonId, Instructor instructor) {

        if (!courseExists(courseId) || !ownsCourse(courseId, instructor) || !lessonExists(courseId, lessonId)) {
            return false;
        }

        for (Course c : courses) {
            if (c.getId() == courseId) {
                c.getLessons().removeIf(l -> l.getId() == lessonId);
                repo.saveCourses(courses);
                return true;
            }
        }

        return false;
    }

    public boolean courseExists(int id) {
        return courses.stream().anyMatch(c -> c.getId() == id);
    }

    public boolean lessonExists(int courseId, int lessonId) {
        for (Course c : courses) {
            if (c.getId() == courseId) {
                return c.getLessons().stream().anyMatch(l -> l.getId() == lessonId);
            }
        }
        return false;
    }

    public boolean ownsCourse(int courseId, Instructor instructor) {
        Instructor real = (Instructor) repo.getUserByEmail(instructor.getEmail());
        return real.getCreatedCourses().contains(courseId);
    }

    public Course getCourse(int id) {
        for (Course c : courses) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public Lesson getLesson(int courseId, int lessonId) {
        for (Course c : courses) {
            if (c.getId() == courseId) {
                for (Lesson l : c.getLessons()) {
                    if (l.getId() == lessonId) {
                        return l;
                    }
                }
            }
        }
        return null;
    }

    public List<Course> getAllCourses() {
        return courses;
    }

    List<String> getEnrolledStudents(int courseId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
