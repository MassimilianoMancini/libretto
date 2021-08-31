package com.example.libretto.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.libretto.repository.ExamRepository;
import com.example.libretto.view.ExamView;

@ExtendWith(MockitoExtension.class)
public class LibrettoControllerTest {
	
	@Mock
	private ExamRepository examRepository;
	
	@Mock
	private ExamView examView;
	
	@InjectMocks
	private LibrettoController librettoController;
	
	
	
	

}
