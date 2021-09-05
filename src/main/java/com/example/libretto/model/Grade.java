package com.example.libretto.model;

import java.util.Objects;

public class Grade {
	
	private String value;
	
	public Grade(String grade) throws IllegalArgumentException {
		if ( ! grade.equals("30L")) {
			try {
				int n = Integer.parseInt(grade);
				if (n < 18) {
					throw new IllegalArgumentException("Insufficient grade");
				}
				if (n > 30) {
					throw new IllegalArgumentException("Too high grade");
				}
			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException("Syntax error");
			}
		}
		this.value = grade;
		
	}

	public String getValue() {
		return value;
	}

	public int getNumericValue() {
		if (this.value.equals("30L")) {
			return 30;
		}
		return Integer.parseInt(this.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grade other = (Grade) obj;
		return Objects.equals(value, other.value);
	}
	
	
}
