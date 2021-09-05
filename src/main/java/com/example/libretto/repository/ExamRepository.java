package com.example.libretto.repository;

import java.util.List;

import com.example.libretto.model.Exam;

public interface ExamRepository {

	public List<Exam> findAll();

	public Exam findById(String id);

	public void save(Exam exam);

	public void delete(String id);

	public void update(Exam exam);

}
