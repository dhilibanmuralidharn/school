package com.example.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.school.repository.StudentRepository;
import com.example.school.model.Student;
import com.example.school.model.StudentRowMapper;

@Service

public class StudentH2Service implements StudentRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Student> getStudents() {
        List<Student> sList = db.query("select * from student", new StudentRowMapper());
        ArrayList<Student> students = new ArrayList<>(sList);
        return students;
    }

    @Override
    public Student getStudentById(int studentId) {
        try {
            Student student2 = db.queryForObject("select * from student where studentId = ?", new StudentRowMapper(),
                    studentId);
            return student2;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Student addStudent(Student student) {
        db.update("insert into student (studentName, gender, standard) values(?, ?, ?)", student.getStudentName(),
                student.getGender(), student.getStandard());
        Student savedStudent = db.queryForObject(
                "select * from student where studentName = ? and gender = ? and standard = ?", new StudentRowMapper(),
                student.getStudentName(), student.getGender(), student.getStandard());
        return savedStudent;
    }

    @Override
    public Student updateStudent (int studentId , Student student){
        if (student.getStudentName() != null){
            db.update("update student set studentName = ? where studentId = ?", student.getStudentName(), studentId);
        }
        if (student.getGender() != null){
            db.update("update student set gender = ? where studentId = ?", student.getGender(), studentId);
        }
        if (student.getStandard() != 0){
            db.update("update student set standard = ? where studentId = ?", student.getStandard(), studentId);
        }
        return getStudentById(studentId);
    }

    @Override
    public void deleteStudent(int studentId){
        db.update("delete from student where studentId = ?", studentId);
    }
}