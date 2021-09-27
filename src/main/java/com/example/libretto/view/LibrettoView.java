package com.example.libretto.view;

import java.util.List;

import com.example.libretto.model.Exam;

public interface LibrettoView {

	void showAllExams(List<Exam> exams);

	void examAdded(Exam exam);
	
	void examRemoved(Exam exam);
	
	void showError(String message);

	void showErrorExamNotFound(String message, Exam	exam);

	void showErrorExamAlreadyExists(String message, Exam exam);

}
