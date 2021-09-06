package com.example.libretto.model.grade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.example.libretto.model.Grade;

class GradeModelTest {
	
	@Test
	void testCreateNewGradeWithCorrectArgumentIsOk() {
		Grade grade = new Grade("20");
		assertThat(grade).isNotNull();
		Grade grade2 = new Grade("18");
		assertThat(grade2).isNotNull();
		Grade grade3 = new Grade("30");
		assertThat(grade3).isNotNull();
	}
	
	@Test
	void testCreateNewGradeWithNotNumericParameterThrows() {
		assertThatThrownBy(()->{new Grade("test");})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Syntax error");
	}
	
	@Test 
	void testCreateNewGradeWith30LParameterIsOk() {
		Grade grade = new Grade("30L");
		assertThat(grade).isNotNull();
	}
	
	@Test
	void testCreateNewGradeWithInsufficientValueThrows() {
		assertThatThrownBy(()->{new Grade("16");})
		.isInstanceOf(IllegalArgumentException.class)
		.hasMessage("Insufficient grade");
	}
	
	@Test
	void testCreateNewGradeWithTooHighValueThrows() {
		assertThatThrownBy(()->{new Grade("31");})
		.isInstanceOf(IllegalArgumentException.class)
		.hasMessage("Too high grade");
	}
	
	@Test
	void testGetNumericValueFromGradeWithoutLaude() {
		Grade grade = new Grade("28");
		assertThat(grade.getNumericValue()).isEqualTo(28);
	}
	
	@Test
	void testGetNumericValueFromGradeWithLaude() {
		Grade grade = new Grade("30L");
		assertThat(grade.getNumericValue()).isEqualTo(30);
	}
}
