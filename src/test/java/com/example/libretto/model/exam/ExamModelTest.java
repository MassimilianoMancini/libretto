package com.example.libretto.model.exam;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.example.libretto.model.Exam;

class ExamModelTest {
	
	@Test
	void testCreateNewExamWithAllPropertiesIsOk() {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, "30", examDate);
		assertThat(exam).isNotNull();
	}

}
