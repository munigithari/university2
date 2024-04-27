/*
 *
 * You can use the following import statements
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 */

// Write your code here
package com.example.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import com.example.university.repository.*;
import com.example.university.model.*;

@Service
public class CourseJpaService implements CourseRepository {
    @Autowired
    private CourseJpaRepository repository;

    @Autowired
    private ProfessorJpaRepository prorepository;

    @Autowired
    private StudentJpaRepository strepository;

    @Override
    public ArrayList<Course> getCourses() {
        List<Course> list = repository.findAll();
        ArrayList<Course> arr = new ArrayList<>(list);
        return arr;
    }

    @Override
    public Course getCourseById(int courseId) {
        try {
            Course course = repository.findById(courseId).get();
            return course;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Course addCourse(Course course) {
        try {
            Professor professor = course.getProfessor();
            int professorId = professor.getProfessorId();
            Professor professors = prorepository.findById(professorId).get();
            course.setProfessor(professors);

            List<Student> students = course.getStudents();
            List<Integer> studentIds = new ArrayList<Integer>();

            for (Student student : students) {
                studentIds.add(student.getStudentId());
            }

            List<Student> existingStudent = strepository.findAllById(studentIds);
            course.setStudents(existingStudent);

            repository.save(course);
            return course;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Course updateCourse(int courseId, Course course) {
        try {
            Course courses = repository.findById(courseId).get();
            if (course.getCourseName() != null) {
                courses.setCourseName(course.getCourseName());
            }
            if (course.getCredits() != 0) {
                courses.setCredits(course.getCredits());
            }
            if (course.getProfessor() != null) {
                Professor professor = course.getProfessor();
                int professorId = professor.getProfessorId();
                Professor professors = prorepository.findById(professorId).get();
                courses.setProfessor(professors);
            }
            if (course.getStudents() != null) {
                List<Student> students = course.getStudents();
                List<Integer> studentIds = new ArrayList<Integer>();

                for (Student student : students) {
                    studentIds.add(student.getStudentId());

                    List<Student> existingStudent = strepository.findAllById(studentIds);
                    course.setStudents(existingStudent);
                }
            }
            repository.save(courses);
            return courses;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteCourse(int courseId) {
        try {
            repository.deleteById(courseId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public Professor getProfessorCourses(int courseId) {
        try {
            Course course = repository.findById(courseId).get();
            Professor prodessor = course.getProfessor();
            return prodessor;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Student> getProfessorStudents(int courseId) {
        try {
            Course course = repository.findById(courseId).get();
            List<Student> students = course.getStudents();
            return students;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
