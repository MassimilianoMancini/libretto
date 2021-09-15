package com.example.libretto.model;

import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;

public class Averages {

	private List<Exam> exams;

	public Averages(List<Exam> exams) {
		if (exams != null) {
			this.exams = exams;
		} else {
			this.exams = Collections.emptyList();
		}
	}

	public Averages(DefaultListModel<Exam> lstExamModel) {
		this.exams = Collections.emptyList();
		for (int i = 0; i < lstExamModel.getSize(); i++) {
			this.exams.add(lstExamModel.get(i));
		}
	}

	public Double getAverage() {
		int n = 0;
		double t = 0;
		for (Exam e : this.exams) {
			t += e.getGrade().getNumericValue();
			n++;
		}
		if (n == 0) {
			return 0.0;
		} else {
			return t / n;
		}
	}

	public Double getWeightedAverage() {
		double t = 0;
		int w = 0;
		for (Exam e : this.exams) {
			w += e.getWeight();
			t += e.getWeight() * e.getGrade().getNumericValue();
		}
		if (w == 0) {
			return 0.0;
		} else {
			return t / w;
		}
	}

}
