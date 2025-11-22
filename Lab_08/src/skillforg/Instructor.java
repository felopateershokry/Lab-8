
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillforg;

import java.util.ArrayList;

/**
 *
 * @author GAMING
 */
public class Instructor extends User {

    private ArrayList<Integer> createdCourses;

    public Instructor(int id, String name, String email, String pass) {
        super(id, name, email, pass, "instructor");
        this.createdCourses = new ArrayList<>();
    }

    public ArrayList<Integer> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(ArrayList<Integer> createdCourses) {
        this.createdCourses = createdCourses;
    }

    public void addCreatedCourse(int id) {
        if (!createdCourses.contains(id)) {
            this.createdCourses.add(id);
        }

    }

    public boolean owns(int courseId) {
        return createdCourses.contains(courseId);
    }
}
