package com.example.libretto.model;

import java.util.Collections;
import java.util.List;

public class Averages {

	private List<Exam> exams;

	public Averages(List<Exam> exams) {
		if (exams != null) {
			this.exams = exams;
		} else {
			this.exams = Collections.emptyList();
		}
	}

	public Double getAverage() {		
		return exams.stream().mapToInt(e -> e.getGrade().getNumericValue()).average().orElse(0);	
	}

	public Double getWeightedAverage() {
		Double t = exams.stream().mapToDouble(e -> e.getGrade().getNumericValue()*e.getWeight()).sum();
		Integer w = exams.stream().mapToInt(Exam::getWeight).sum();
		return (w != 0 ? t/w : 0);
		
	}

}
