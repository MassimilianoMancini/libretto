package com.example.libretto.repository.mariadb;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;


class ExamMariaDBRepositoryTest {
	
	private static final String LIBRETTO_DB_NAME = "librettotest";
	private static DB db;
	private static DBConfigurationBuilder config;
	private Connection conn;
	private Statement stmt;
	private ExamMariaDBRepository examRepository;
	
	@BeforeAll
	static void setupServer() throws ManagedProcessException {

		config = DBConfigurationBuilder.newBuilder();
		config.setPort(0);
		
		db = DB.newEmbeddedDB(config.build());
		db.start();
	}
	
	@BeforeEach
	void setup() throws ManagedProcessException, SQLException {
		db.createDB(LIBRETTO_DB_NAME);
		conn = DriverManager.getConnection(config.getURL(LIBRETTO_DB_NAME), "root", "");
		examRepository = new ExamMariaDBRepository(conn);
		
		stmt = conn.createStatement();
		stmt.executeUpdate("drop database " + LIBRETTO_DB_NAME);
		stmt.executeUpdate("create database " + LIBRETTO_DB_NAME);
		stmt.executeUpdate("use " + LIBRETTO_DB_NAME);
		stmt.executeUpdate(
				"create table libretto ("
				+ "id varchar(7) not null primary key, "
				+ "description varchar(60) not null,"
				+ "weight int not null,"
				+ "grade varchar(3) not null,"
				+ "date date not null"
				+ ")");
		
	}
	

	@AfterEach
	void tearDown() throws SQLException {
		stmt.close();
		conn.close();
		
	}

	@AfterAll
	static void shutdownServer() throws ManagedProcessException {
		db.stop();
	}
	
	@Test
	void testEmbeddedMariaDB4j() throws SQLException {
		stmt.executeUpdate("insert into libretto values ("
				+ "'B027500', "
				+ "'Data Mining and Organization', "
				+ "12, "
				+ "'30L', "
				+ "'2020-01-29')");
		
		stmt.executeUpdate("select description from libretto");
		ResultSet rs = stmt.getResultSet();
		rs.next();
		assertThat(rs.getString("description")).isEqualTo("Data Mining and Organization");
	}
	
	@Test
	void testFindAllWhenDatabaseIsEmpty() throws SQLException {
		assertThat(examRepository.findAll()).isEmpty();
	}
	
	@Test
	void testFindAllWhenDatabaseIsNotEmpty() throws SQLException {
		stmt.executeUpdate("insert into libretto values ("
				+ "'B027500', "
				+ "'Data Mining and Organization', "
				+ "12, "
				+ "'30L', "
				+ "'2020-01-29')");
		
		stmt.executeUpdate("insert into libretto values ("
				+ "'B027507', "
				+ "'Parallel Computing', "
				+ "6, "
				+ "'27', "
				+ "'2020-01-09')");
		assertThat(examRepository.findAll()).containsExactly(
			new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"), LocalDate.of(2020, 1, 29)),
			new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
	}
	
	@Test
	void testFindByIdNotFound() throws SQLException {
		assertThat(examRepository.findById("B027500")).isNull();
	}
	
	@Test
	void testFindByIdFound() throws SQLException {
		stmt.executeUpdate("insert into libretto values ("
				+ "'B027500', "
				+ "'Data Mining and Organization', "
				+ "12, "
				+ "'30L', "
				+ "'2020-01-29')");
		
		stmt.executeUpdate("insert into libretto values ("
				+ "'B027507', "
				+ "'Parallel Computing', "
				+ "6, "
				+ "'27', "
				+ "'2020-01-09')");
		
		assertThat(examRepository.findById("B027507")).isEqualTo(
			new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
		
	}
	
	@Test
	void testSave() throws IllegalArgumentException, SQLException {
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"), LocalDate.of(2020, 1, 29)); 
		
		examRepository.save(exam);
		assertThat(readAllExamsFromDatabase()).containsExactly(exam);
		
	}
	
	@Test
	void testDelete() throws SQLException {
		stmt.executeUpdate("insert into libretto values ("
				+ "'B027500', "
				+ "'Data Mining and Organization', "
				+ "12, "
				+ "'30L', "
				+ "'2020-01-29')");
		examRepository.delete("B027500");
		assertThat(readAllExamsFromDatabase()).isEmpty();;
		
	}
	
	@Test
	void testUpdate() throws SQLException {
		stmt.executeUpdate("insert into libretto values ("
				+ "'B027500', "
				+ "'Data Mining and Organization', "
				+ "12, "
				+ "'28', "
				+ "'2020-01-29')");
		
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"), LocalDate.of(2020, 1, 29)); 
		examRepository.update(exam);
		assertThat(readAllExamsFromDatabase()).containsExactly(exam);
	}

	private List<Exam> readAllExamsFromDatabase() throws SQLException, IllegalArgumentException {
		List<Exam> examList = new ArrayList<>();
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("select * from libretto");
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				examList.add(newExamFromResultset(rs));
			}
		}
		return examList;
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
