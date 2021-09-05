package com.example.libretto.controller;

import com.example.libretto.model.Exam;
import com.example.libretto.repository.ExamRepository;
import com.example.libretto.view.LibrettoView;

public class LibrettoController {
	
	private LibrettoView librettoView;
	private ExamRepository examRepository;

	public void allExams() {
		librettoView.showAllExams(examRepository.findAll());
	}

	public void newExam(Exam exam) {
		// TODO Auto-generated method stub
		
	}

}
