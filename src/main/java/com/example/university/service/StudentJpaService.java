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

import com.example.university.model.Course;
import com.example.university.model.Student;
import com.example.university.repository.*;

@Service
public class StudentJpaService implements StudentRepository {
    @Autowired
    private CourseJpaRepository repository;

    @Autowired
    private StudentJpaRepository strepository;

    @Override
    public ArrayList<Student> getStudents() {
        List<Student> list = strepository.findAll();
        ArrayList<Student> arr = new ArrayList<>(list);
        return arr;
    }

    @Override
    public Student getStudentById(int studentId) {
        try {
            Student student = strepository.findById(studentId).get();
            return student;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Student addStudent(Student student) {
        List<Integer> couseIds = new ArrayList<>();
        for (Course course : student.getCourses()) {
            couseIds.add(course.getCourseId());
        }

        List<Course> courses = repository.findAllById(couseIds);

        if (courses.size() != couseIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        student.setCourses(courses);

        for (Course course : courses) {
            course.getStudents().add(student);
        }

        repository.saveAll(courses);

        strepository.save(student);

        return student;

    }

    @Override
    public Student updateStudent(int studentId, Student student) {
        try {
            Student students = strepository.findById(studentId).get();
            if (student.getStudentName() != null) {
                students.setStudentName(student.getStudentName());
            }
            if (student.getEmail() != null) {
                students.setEmail(student.getEmail());
            }
            if (student.getCourses() != null) {
                List<Course> existingCourse = students.getCourses();

                for (Course course : existingCourse) {
                    course.getStudents().remove(students);
                }

                repository.saveAll(existingCourse);

                List<Integer> courseIds = new ArrayList<>();

                for (Course course : student.getCourses()) {
                    courseIds.add(course.getCourseId());
                }

                List<Course> courses = repository.findAllById(courseIds);

                if (courses.size() != courseIds.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }

                student.setCourses(courses);

                for (Course course : courses) {
                    course.getStudents().add(student);
                }

                repository.saveAll(courses);
            }
            strepository.save(students);
            return students;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public void deleteStudent(int studentId) {
        try {
            Student student = strepository.findById(studentId).get();

            List<Course> cousres = student.getCourses();

            for (Course course : cousres) {
                course.getStudents().remove(student);
            }

            repository.saveAll(cousres);
            strepository.deleteById(studentId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Course> getCourseStudent(int studentId) {
        try {
            Student student = strepository.findById(studentId).get();
            List<Course> course = student.getCourses();
            return course;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}