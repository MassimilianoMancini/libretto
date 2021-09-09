package com.example.libretto.repository.mariadb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;
import com.example.libretto.repository.ExamRepository;

public class ExamMariaDBRepository implements ExamRepository {
	
	private Connection conn;

	public ExamMariaDBRepository(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<Exam> findAll() throws SQLException {
		List<Exam> examList = new ArrayList<>();
		Statement stmt = conn.createStatement();
		try {
			stmt.executeUpdate("select * from libretto");
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				examList.add(new Exam(
					rs.getString("id"), 
					rs.getString("description"),
					rs.getInt("weight"),
					new Grade(rs.getString("grade")),
					LocalDate.parse(rs.getString("date"))
				));
			}
		} finally {
			stmt.close();
		}
		return examList;

		
	}

	@Override
	public Exam findById(String id) throws SQLException {
		Statement stmt = conn.createStatement();
		try {
			stmt.executeUpdate("select * from libretto where id = '" + id + "'");
			ResultSet rs = stmt.getResultSet();
			if (rs.next()) {
				return new Exam(
					rs.getString("id"), 
					rs.getString("description"),
					rs.getInt("weight"),
					new Grade(rs.getString("grade")),
					LocalDate.parse(rs.getString("date")));
			} else {
				return null;
			}
		} finally {
			stmt.close();
		}
	}

	@Override
	public void save(Exam exam) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Exam exam) {
		// TODO Auto-generated method stub
		
	}

}
