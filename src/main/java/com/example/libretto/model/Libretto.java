package com.example.libretto.model;

import java.util.List;

public class Libretto {
	
	private List<Exam> exams;

	public Libretto(List<Exam> exams) {
		this.exams = exams;
	}

	public double getAverage() {
		int n = 0;
		double t = 0;
		for (Exam e : this.exams) {
			t += e.getGrade().getNumericValue();
			n++;
		}
		if (n == 0) {
			return 0;
		} else {
			return t/n;
		}
	}

	public double getWeightedAverage() {
		double t = 0;
		int w = 0;
		for (Exam e : this.exams) {
			w += e.getWeight();
			t += e.getWeight() * e.getGrade().getNumericValue();
		}
		if (w == 0) {
			return 0;
		} else {
			return t/w;
		}
	}

}
