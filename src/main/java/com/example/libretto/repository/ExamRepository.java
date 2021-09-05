package com.example.libretto.repository;

import java.util.List;

import com.example.libretto.model.Exam;

public interface ExamRepository {

	public List<Exam> findAll();

	public Exam findById(String id);

	public void save(Exam exam);

}
