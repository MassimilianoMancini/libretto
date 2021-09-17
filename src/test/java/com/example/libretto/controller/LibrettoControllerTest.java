package com.example.libretto.controller;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;

import java.sql.SQLException;
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
	void testAllExams() throws SQLException {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		
		List<Exam> exams = asList(exam);
		when(examRepository.findAll()).thenReturn(exams);
		librettoController.allExams();
		verify(librettoView).showAllExams(exams);
	}
	
	@Test
	void testNewExamWhenExamDoesNotAlreadyExists() throws SQLException {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		librettoController.newExam(exam);
		InOrder inOrder = inOrder(examRepository, librettoView);
		inOrder.verify(examRepository).save(exam);
		inOrder.verify(librettoView).examAdded(exam);
	}
	
	@Test
	void testNewExamWhenExamAlreadyExists() throws SQLException {
		LocalDate examDate1 = LocalDate.of(2020, 1, 29);
		Grade grade1 = new Grade("30");
		Exam examToAdd = new Exam("B027500", "Data Mining and Organization", 12, grade1, examDate1);
		
		LocalDate examDate2 = LocalDate.of(2020, 1, 29);
		Grade grade2 = new Grade("28");
		Exam existingExam = new Exam("B027500", "Parallel Computing", 6, grade2, examDate2);
		
		when(examRepository.findById("B027500")).thenReturn(existingExam);
		librettoController.newExam(examToAdd);
		verify(librettoView).showError("Already existing exam with id B027500", existingExam);
		verifyNoMoreInteractions(ignoreStubs(examRepository));
	}
	
	@Test
	void testDeleteExamWhenExamExists() throws SQLException {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam examToDelete = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		when(examRepository.findById("B027500")).thenReturn(examToDelete);
		librettoController.deleteExam(examToDelete);
		InOrder inOrder = inOrder(examRepository, librettoView);
		inOrder.verify(examRepository).delete("B027500");
		inOrder.verify(librettoView).examRemoved(examToDelete);
	}
	
	@Test
	void testDeleteExamWhenExamDoesNotExists() throws SQLException {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		when(examRepository.findById("B027500")).thenReturn(null);
		librettoController.deleteExam(exam);
		verify(librettoView).showError("No existing exam with id B027500", exam);
		verifyNoMoreInteractions(ignoreStubs(examRepository));	
	}
}
