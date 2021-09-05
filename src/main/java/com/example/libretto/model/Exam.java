package com.example.libretto.model;

import java.time.LocalDate;

public class Exam {
	
	private String id;
	private String description;
	private int weight;
	private String grade;
	private LocalDate date;

	public Exam(String id, String description, int weight, String grade, LocalDate date) {
		this.id = id;
		this.description = description;
		this.weight = weight;
		this.grade = grade;
		this.date = date;
	}
	
	
}
