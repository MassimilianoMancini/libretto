package com.example.libretto.controller;

import java.sql.SQLException;

import com.example.libretto.model.Exam;
import com.example.libretto.repository.ExamRepository;
import com.example.libretto.view.LibrettoView;

public class LibrettoController {
	
	private static final String SQL_ERROR = "Errore SQL";
	private LibrettoView librettoView;
	private ExamRepository examRepository;

	public LibrettoController(LibrettoView librettoView, ExamRepository examRepository) {
		this.librettoView = librettoView;
		this.examRepository = examRepository;
	}

	public void allExams() {
		try {
			librettoView.showAllExams(examRepository.findAll());
		} catch (SQLException e) {
			librettoView.showError(SQL_ERROR);
		}
	}

	public void newExam(Exam exam) {
		try {
			Exam existingExam = examRepository.findById(exam.getId());
			if (existingExam != null) {
				librettoView.showErrorExamAlreadyExists("Esame già presente con codice " + exam.getId(), existingExam);
				return;
			}
			examRepository.save(exam);
			librettoView.examAdded(exam);
		} catch (SQLException e) {
			librettoView.showError(SQL_ERROR);
		}		
	}

	public void updateExam(Exam exam) {
		try {
			Exam oldExam = examRepository.findById(exam.getId());
			if (oldExam == null) {
				librettoView.showErrorExamNotFound("Esame inesistente con codice " + exam.getId(), exam);
			} else {
				librettoView.examRemoved(oldExam);
				examRepository.update(exam);
				librettoView.examAdded(exam);
			}
		} catch (SQLException e) {
			librettoView.showError(SQL_ERROR);
		}
	}

	public void deleteExam(Exam exam) {
		try {
			if (examRepository.findById(exam.getId()) == null) {
				librettoView.showErrorExamNotFound("Esame inesistente con codice " + exam.getId(), exam);
				return;
			}
			examRepository.delete(exam.getId());
			librettoView.examRemoved(exam);
		} catch (SQLException e) {
			librettoView.showError(SQL_ERROR);
		}
	}
}