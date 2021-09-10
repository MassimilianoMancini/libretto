package com.example.libretto.repository;

import java.sql.SQLException;
import java.util.List;

import com.example.libretto.model.Exam;

public interface ExamRepository {

	public List<Exam> findAll() throws SQLException;

	public Exam findById(String id) throws SQLException;

	public void save(Exam exam) throws SQLException;

	public void delete(String id);

	public void update(Exam exam);

}
