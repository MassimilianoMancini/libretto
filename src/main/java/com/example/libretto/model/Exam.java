package com.example.libretto.model;

import java.time.LocalDate;
import java.util.Objects;

import com.example.libretto.Generated;

public class Exam {
	
	private static final String FUTURE_DATE_ERROR_MESSAGE = "Non sono ammesse date future";
	private String id;
	private String description;
	private int weight;
	private Grade grade;
	private LocalDate date;

	public Exam(String id, String description, int weight, Grade grade, LocalDate date) throws IllegalArgumentException {
		this.id = id;
		this.description = description;
		this.weight = weight;
		this.grade = grade;
		if (date.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException(FUTURE_DATE_ERROR_MESSAGE);
		}
		this.date = date;
	}

	@Generated
	public String getId() {
		return id;
	}

	@Generated
	public void setId(String id) {
		this.id = id;
	}

	@Generated
	public String getDescription() {
		return description;
	}

	@Generated
	public void setDescription(String description) {
		this.description = description;
	}

	@Generated
	public int getWeight() {
		return weight;
	}

	@Generated
	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Generated
	public Grade getGrade() {
		return grade;
	}

	@Generated
	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	@Generated
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) throws IllegalArgumentException {
		if (date.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException(FUTURE_DATE_ERROR_MESSAGE); 
		}
		this.date = date;
	}

	@Generated
	@Override
	public int hashCode() {
		return Objects.hash(date, description, grade, id, weight);
	}

	@Generated
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exam other = (Exam) obj;
		return Objects.equals(date, other.date) && Objects.equals(description, other.description)
				&& Objects.equals(grade, other.grade) && Objects.equals(id, other.id) && weight == other.weight;
	}

	@Override
	public String toString() {
		return "Exam [id=" + id + ", description=" + description + ", weight=" + weight + ", grade=" + grade.getValue() + ", date=" + date + "]";
	}
	
	
	
	
}
