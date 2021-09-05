package com.example.libretto.model.exam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;

class ExamModelTest {
	
	@Test
	void testCreateNewExamWithAllPropertiesIsOk() {
		LocalDate examDate = LocalDate.of(2020, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		assertThat(exam).isNotNull();
	}
	
	@Test
	void testCreateNewExamWithFutureDateThrows() {
		LocalDate examDate = LocalDate.of(2022, 1, 29);
		Grade grade = new Grade("30");
		assertThatThrownBy(() -> new Exam("B027500", "Data Mining and Organization", 12, grade, examDate))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Only past dates are allowed");
	}
	
	@Test
	void testSetExamDateToFutureThrows() {
		LocalDate examDate = LocalDate.of(2021, 1, 29);
		Grade grade = new Grade("30");
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
		LocalDate newDate = LocalDate.of(2022, 3, 10);
		
		assertThatThrownBy(() -> exam.setDate(newDate))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Only past dates are allowed");
	}
}
