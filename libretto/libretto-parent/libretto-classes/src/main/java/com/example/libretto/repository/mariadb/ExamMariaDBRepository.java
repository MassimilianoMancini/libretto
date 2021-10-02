package com.example.libretto.repository.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
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
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("select * from libretto order by date");
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				examList.add(newExamFromResultset(rs));
			}
		}
		return examList;
	}

	@Override
	public Exam findById(String id) throws SQLException {
		String query = "select * from libretto where id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return newExamFromResultset(rs);
			} else {
				return null;
			}
		}
	}


	@Override
	public void save(Exam exam) throws SQLException {
		String query = "insert into libretto values (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, exam.getId());
			pstmt.setString(2, exam.getDescription()); 
			pstmt.setInt(3, exam.getWeight());
			pstmt.setString(4, exam.getGrade().getValue());
			pstmt.setDate(5, Date.valueOf(exam.getDate()));
			pstmt.executeQuery();
		}
	}

	@Override
	public void delete(String id) throws SQLException {
		String query = "delete from libretto where id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, id);
			pstmt.executeQuery();
		}
		
	}

	@Override
	public void update(Exam exam) throws SQLException {
		String query = "update libretto set description=?, weight=?, grade=?, date=? where id=?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {			
			pstmt.setString(1, exam.getDescription()); 
			pstmt.setInt(2, exam.getWeight());
			pstmt.setString(3, exam.getGrade().getValue());
			pstmt.setDate(4, Date.valueOf(exam.getDate()));
			pstmt.setString(5, exam.getId());
			pstmt.executeQuery();
		}
		
	}

	private Exam newExamFromResultset(ResultSet rs) throws IllegalArgumentException, SQLException {
		return new Exam(
				rs.getString("id"), 
				rs.getString("description"),
				rs.getInt("weight"),
				new Grade(rs.getString("grade")),
				LocalDate.parse(rs.getString("date")));
	}
}