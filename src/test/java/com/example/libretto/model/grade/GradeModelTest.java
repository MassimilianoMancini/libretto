package com.example.libretto.model.grade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.libretto.model.Grade;

@DisplayName("Tests for Grade")
class GradeModelTest {

	@Nested
	@DisplayName("Happy cases")
	class HappyCases {
		@Test @DisplayName("Create with numeric value")
		void testCreateNewGradeWithCorrectArgumentIsOk() {
			Grade grade = new Grade("20");
			assertThat(grade).isNotNull();
			Grade grade2 = new Grade("18");
			assertThat(grade2).isNotNull();
			Grade grade3 = new Grade("30");
			assertThat(grade3).isNotNull();
		}

		@Test @DisplayName("Create with 30L") 
		void testCreateNewGradeWith30LParameterIsOk() {
			Grade grade = new Grade("30L");
			assertThat(grade).isNotNull();
		}

		@Test @DisplayName("Get numeric value")
		void testGetNumericValueFromGradeWithoutLaude() {
			Grade grade = new Grade("28");
			assertThat(grade.getNumericValue()).isEqualTo(28);
		}

		@Test @DisplayName("Get 30L")
		void testGetNumericValueFromGradeWithLaude() {
			Grade grade = new Grade("30L");
			assertThat(grade.getNumericValue()).isEqualTo(30);
		}
	}

	@Nested
	@DisplayName("Fail cases")
	class FailCases {
		@Test @DisplayName("Create with string")
		void testCreateNewGradeWithNotNumericParameterThrows() {
			assertThatThrownBy(()->{new Grade("test");})
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Errore di sintassi");
		}

		@Test @DisplayName("Too low value")
		void testCreateNewGradeWithInsufficientValueThrows() {
			assertThatThrownBy(()->{new Grade("16");})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Voto troppo basso");
		}

		@Test @DisplayName("Too high value")
		void testCreateNewGradeWithTooHighValueThrows() {
			assertThatThrownBy(()->{new Grade("31");})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Voto troppo alto");
		}
	}
}