package com.example.libretto.controller;

import java.sql.SQLException;

import com.example.libretto.model.Exam;
import com.example.libretto.repository.ExamRepository;
import com.example.libretto.view.LibrettoView;

public class LibrettoController {
	
	private LibrettoView librettoView;
	private ExamRepository examRepository;

	public void allExams() throws SQLException {
		librettoView.showAllExams(examRepository.findAll());
	}

	public void newExam(Exam exam) throws SQLException {
		Exam existingExam = examRepository.findById(exam.getId());
		if (existingExam != null) {
			librettoView.showError("Already existing exam with id " + exam.getId(), existingExam);
			return;
		}
		examRepository.save(exam);
		librettoView.examAdded(exam);
		
	}

	public void deleteExam(Exam exam) throws SQLException {
		if (examRepository.findById(exam.getId()) == null) {
			librettoView.showError("No existing exam with id " + exam.getId(), exam);
			return;
		}
		examRepository.delete(exam.getId());
		librettoView.examRemoved(exam);
	}

	public void updateExam(Exam exam) throws SQLException {
		if (examRepository.findById(exam.getId()) == null) {
			librettoView.showError("No existing exam with id " + exam.getId(), exam);
			return;
		}
		examRepository.update(exam);
		librettoView.examUpdated(exam);
		
	}

}
