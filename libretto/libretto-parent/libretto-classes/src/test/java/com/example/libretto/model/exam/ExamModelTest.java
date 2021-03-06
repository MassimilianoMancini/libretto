package com.example.libretto.model.exam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;

@DisplayName("Tests for Exam")
class ExamModelTest {

	private static final String FUTURE_DATE_ERROR_MESSAGE = "Non sono ammesse date future";

	@Nested
	@DisplayName("Happy cases")
	class HappyCases {
		@Test @DisplayName("Create")
		void testCreateNewExamWithAllPropertiesIsOk() {
			LocalDate examDate = LocalDate.of(2020, 1, 29);
			Grade grade = new Grade("30");
			Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
			assertThat(exam).isNotNull();
		}

		@Test @DisplayName("Set date to present")
		void testSetExamDateToPresentIsOk() {
			LocalDate examDate = LocalDate.of(2021, 1, 29);
			Grade grade = new Grade("30");
			Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
			LocalDate newDate = LocalDate.now();
			exam.setDate(newDate);
			assertThat(exam.getDate()).isEqualTo(newDate);
		}

		@Test @DisplayName("Set date to past")
		void testSetExamDateToPastIsOk() {
			LocalDate examDate = LocalDate.of(2021, 1, 29);
			Grade grade = new Grade("30");
			Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
			LocalDate newDate = LocalDate.of(2020, 5, 2);
			exam.setDate(newDate);
			assertThat(exam.getDate()).isEqualTo(newDate);
		}

		@Test @DisplayName("Get Grade value") 
		void testExamToStringGetGradeValue() {
			LocalDate examDate = LocalDate.of(2021, 1, 29);
			Grade grade = new Grade("30L");
			Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
			assertThat(exam).hasToString("Exam [id=B027500, description=Data Mining and Organization, weight=12, grade=30L, date=" + examDate + "]");
			
		}
	}

	@Nested
	@DisplayName("Fail cases")
	class FailCases {	
		@Test @DisplayName("Create with future date")
		void testCreateNewExamWithFutureDateThrows() {
			LocalDate examDate = LocalDate.of(2022, 1, 29);
			Grade grade = new Grade("30");
			assertThatThrownBy(() -> new Exam("B027500", "Data Mining and Organization", 12, grade, examDate))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FUTURE_DATE_ERROR_MESSAGE);
		}

		@Test @DisplayName("Set date to future")
		void testSetExamDateToFutureThrows() {
			LocalDate examDate = LocalDate.of(2021, 1, 29);
			Grade grade = new Grade("30");
			Exam exam = new Exam("B027500", "Data Mining and Organization", 12, grade, examDate);
			LocalDate newDate = LocalDate.of(2022, 3, 10);
			
			assertThatThrownBy(() -> exam.setDate(newDate))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(FUTURE_DATE_ERROR_MESSAGE);
		}
	}

}