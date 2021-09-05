package com.example.libretto.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.libretto.model.Exam;
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
		List<Exam> exams = java.util.Arrays.asList(new Exam());
		when(examRepository.findAll()).thenReturn(exams);
		librettoController.allExams();
		verify(librettoView).showAllExams(exams);
	}
	
	
	

}
