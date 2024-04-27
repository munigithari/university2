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
import com.example.university.model.Professor;
import com.example.university.repository.*;

@Service
public class ProfessorJpaService implements ProfessorRepository {

    @Autowired
    private CourseJpaRepository repository;

    @Autowired
    private ProfessorJpaRepository prorepository;

    @Override
    public ArrayList<Professor> getProfessors() {
        List<Professor> list = prorepository.findAll();
        ArrayList<Professor> arr = new ArrayList<>(list);
        return arr;
    }

    @Override
    public Professor getProfessorById(int professorId) {
        try {
            Professor professor = prorepository.findById(professorId).get();
            return professor;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Professor addProfessor(Professor professor) {
        return prorepository.save(professor);

    }

    @Override
    public Professor updateProfessor(int professorId, Professor professor) {
        try {
            Professor professors = prorepository.findById(professorId).get();
            if (professor.getProfessorName() != null) {
                professors.setProfessorName(professor.getProfessorName());
            }
            if (professor.getDepartment() != null) {
                professors.setDepartment(professor.getDepartment());
            }
            prorepository.save(professors);
            return professors;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteProfessor(int professorId) {
        try {
            Professor professor = prorepository.findById(professorId).get();

            List<Course> courses = repository.findByProfessor(professor);

            for (Course course : courses) {
                course.setProfessor(null);
            }

            repository.saveAll(courses);

            prorepository.deleteById(professorId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Course> getProfessorCourse(int professorId) {
        try {
            Professor professor = prorepository.findById(professorId).get();
            List<Course> courses = repository.findByProfessor(professor);
            return courses;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}