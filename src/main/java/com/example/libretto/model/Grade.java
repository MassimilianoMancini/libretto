package com.example.libretto.model;

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
}
