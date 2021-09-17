package com.example.libretto.model.averages;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import com.example.libretto.model.Averages;
import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;

class AveragesModelTest {
	
	@Test
	void testGetAverage() {
		LocalDate examDate1 = LocalDate.of(2020, 1, 29);
		Grade grade1 = new Grade("30");
		Exam exam1 = new Exam("B027500", "Data Mining and Organization", 12, grade1, examDate1);
		
		LocalDate examDate2 = LocalDate.of(2020, 1, 9);
		Grade grade2 = new Grade("27");
		Exam exam2 = new Exam("B027507", "Parallel Computing", 6, grade2, examDate2);
		
		LocalDate examDate3 = LocalDate.of(2020, 6, 15);
		Grade grade3 = new Grade("26");
		Exam exam3 = new Exam("B027500", "Numerical Methods for Graphics", 6, grade3, examDate3);
		
		List<Exam> exams = asList(exam1, exam2, exam3);
		Averages averages = new Averages(exams);
		
		assertThat(averages.getAverage()).isEqualTo((30+27+26)/3.0);		
	}
	
	@Test
	void testGetWeighedAverage() {
		LocalDate examDate1 = LocalDate.of(2020, 1, 29);
		Grade grade1 = new Grade("30");
		Exam exam1 = new Exam("B027500", "Data Mining and Organization", 12, grade1, examDate1);
		
		LocalDate examDate2 = LocalDate.of(2020, 1, 9);
		Grade grade2 = new Grade("27");
		Exam exam2 = new Exam("B027507", "Parallel Computing", 6, grade2, examDate2);
		
		LocalDate examDate3 = LocalDate.of(2020, 6, 15);
		Grade grade3 = new Grade("26");
		Exam exam3 = new Exam("B027500", "Numerical Methods for Graphics", 6, grade3, examDate3);
		
		List<Exam> exams = asList(exam1, exam2, exam3);
		Averages averages = new Averages(exams);
		
		assertThat(averages.getWeightedAverage()).isEqualTo((30*12+27*6+26*6)/(12.0+6.0+6.0));		
	}
	
	@Test
	void testAverageOnEmptyListIsZero() {
		List<Exam> exams = Lists.emptyList();
		Averages averages = new Averages(exams);
		assertThat(averages.getAverage()).isZero();
	}
	
	@Test
	void testWeightedAverageOnEmptyListIsZero() {
		List<Exam> exams = Lists.emptyList();
		Averages averages = new Averages(exams);
		assertThat(averages.getWeightedAverage()).isZero();
	}
	
}
