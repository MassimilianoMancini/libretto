package com.example.libretto.controller;

import static org.mockito.Mockito.doThrow;
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
		verify(librettoView).showErrorExamAlreadyExists("Esame già presente con codice B027500", existingExam);
		verifyNoMoreInteractions(ignoreStubs(examRepository));
	}
	
	@Test
	void testUpdateExamWhenExists() throws SQLException {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam examToUpdate = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		Exam examUpdated = new Exam("B027500", "Data Science", 6, grade, examDate);
		when(examRepository.findById("B027500")).thenReturn(examToUpdate);
		librettoController.updateExam(examUpdated);
		InOrder inOrder = inOrder(examRepository, librettoView);
		inOrder.verify(librettoView).examRemoved(examToUpdate);
		inOrder.verify(examRepository).update(examUpdated);
		inOrder.verify(librettoView).examAdded(examUpdated);
	}
	
	@Test
	void testUpdateExamWhenDoesNotExists() throws SQLException {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		when(examRepository.findById("B027500")).thenReturn(null);
		librettoController.updateExam(exam);
		verify(librettoView).showErrorExamNotFound("Esame inesistente con codice B027500", exam);
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
		verify(librettoView).showErrorExamNotFound("Esame inesistente con codice B027500", exam);
		verifyNoMoreInteractions(ignoreStubs(examRepository));	
	}
	
	@Test
	void testAllExamsThrows() throws SQLException {
		when(examRepository.findAll()).thenThrow(SQLException.class);
		librettoController.allExams();
		verify(librettoView).showError("Errore SQL");
	}
	
	@Test
	void testNewExamsThrows() throws SQLException {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		doThrow(SQLException.class).when(examRepository).save(exam);
		librettoController.newExam(exam);
		verify(librettoView).showError("Errore SQL");
	}
	
	@Test
	void testUpdateExamsThrows() throws SQLException {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		when(examRepository.findById("B027500")).thenReturn(exam);
		doThrow(SQLException.class).when(examRepository).update(exam);
		librettoController.updateExam(exam);
		verify(librettoView).showError("Errore SQL");
	}
	
	@Test
	void testDeleteExamThrows() throws SQLException {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam examToDelete = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		when(examRepository.findById("B027500")).thenReturn(examToDelete);
		doThrow(SQLException.class).when(examRepository).delete("B027500");
		librettoController.deleteExam(examToDelete);
		verify(librettoView).showError("Errore SQL");
	}

	
}
