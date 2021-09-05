package com.example.libretto.controller;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;
import com.example.libretto.repository.ExamRepository;
import com.example.libretto.view.LibrettoView;

@ExtendWith(MockitoExtension.class)
class LibrettoControllerTest {
	
	@Mock
	private ExamRepository examRepository;
	
	@Mock
	private LibrettoView librettoView;
	
	@InjectMocks
	private LibrettoController librettoController;
	
	@Test
	void testAllExams() {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		
		List<Exam> exams = asList(exam);
		when(examRepository.findAll()).thenReturn(exams);
		librettoController.allExams();
		verify(librettoView).showAllExams(exams);
	}
	
	@Test
	void testNewExamWhenExamDoesNotAlreadyExists() {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		when(examRepository.findById("B027500")).thenReturn(null);
		librettoController.newExam(exam);
		InOrder inOrder = inOrder(examRepository, librettoView);
		inOrder.verify(examRepository).save(exam);
		inOrder.verify(librettoView).examAdded(exam);
	}
	
	
}
