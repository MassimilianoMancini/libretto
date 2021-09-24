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
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;

// If run in Eclipse, an up and running MariDB server is needed
// for example via 'docker run --rm -p 3306:3306 -e MARIADB_ROOT_PASSWORD=password mariadb:10.6.4'

class ExamMariaDBRepositoryIT {

	private static final String LIBRETTO_DB_NAME = "librettotest";
	private static Connection conn = null;
	private static int mariadbPort = Integer.parseInt(System.getProperty("mariadb.port", "3306"));
	private static String mariadbPassword = System.getProperty("MARIADB_ROOT_PASSWORD", "password");
	private Statement stmt;
	private ExamMariaDBRepository examRepository;


	@BeforeAll
	static void connectToDB() throws InterruptedException {
		for (int i = 1; (conn == null) && (i < 60); i++) {
			try {
				conn = DriverManager.getConnection("jdbc:mariadb://localhost:" + mariadbPort, "root", mariadbPassword);
			} catch (SQLException e) {
				System.out.println("[ExamMariaDBRepositoryIT] DB connection attempt # " + i);
				TimeUnit.SECONDS.sleep(1);
			}
			if (conn != null && i > 1) {
				System.out.println("[ExamMariaDBRepositoryIT] Connected to DB!");
			}
		}
	}

	@BeforeEach
	void setup() throws SQLException {
		stmt = conn.createStatement();
		stmt.executeUpdate("drop database if exists " + LIBRETTO_DB_NAME);
		stmt.executeUpdate("create database " + LIBRETTO_DB_NAME);
		stmt.executeUpdate("use " + LIBRETTO_DB_NAME);
		stmt.executeUpdate(
				"create table libretto (" + "id varchar(7) not null primary key, " + "description varchar(60) not null,"
						+ "weight int not null," + "grade varchar(3) not null," + "date date not null" + ")");
		
		examRepository = new ExamMariaDBRepository(conn);
	}

	@AfterEach
	void tearDown() throws SQLException {
		stmt.close();

	}

	@AfterAll
	static void closeConnection() throws SQLException {
		conn.close();
	}

	@Test
	void testMariaDBConnection() throws SQLException {
		stmt.executeUpdate("insert into libretto values (" + "'B027500', " + "'Data Mining and Organization', " + "12, "
				+ "'30L', " + "'2020-01-29')");

		stmt.executeUpdate("select description from libretto");
		ResultSet rs = stmt.getResultSet();
		rs.next();
		assertThat(rs.getString("description")).isEqualTo("Data Mining and Organization");
	}

	@Test
	void testFindAll() throws SQLException {
		stmt.executeUpdate("insert into libretto values (" + "'B027500', " + "'Data Mining and Organization', " + "12, "
				+ "'30L', " + "'2020-01-29')");

		stmt.executeUpdate("insert into libretto values (" + "'B027507', " + "'Parallel Computing', " + "6, " + "'27', "
				+ "'2020-01-09')");
		assertThat(examRepository.findAll()).containsExactly(
				new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"), LocalDate.of(2020, 1, 29)),
				new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
	}

	@Test
	void testFindById() throws SQLException {
		stmt.executeUpdate("insert into libretto values (" + "'B027500', " + "'Data Mining and Organization', " + "12, "
				+ "'30L', " + "'2020-01-29')");

		stmt.executeUpdate("insert into libretto values (" + "'B027507', " + "'Parallel Computing', " + "6, " + "'27', "
				+ "'2020-01-09')");

		assertThat(examRepository.findById("B027507"))
				.isEqualTo(new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));

	}

	@Test
	void testSave() throws IllegalArgumentException, SQLException {
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));

		examRepository.save(exam);
		assertThat(readAllExamsFromDatabase()).containsExactly(exam);

	}

	@Test
	void testDelete() throws SQLException {
		stmt.executeUpdate("insert into libretto values (" + "'B027500', " + "'Data Mining and Organization', " + "12, "
				+ "'30L', " + "'2020-01-29')");
		examRepository.delete("B027500");
		assertThat(readAllExamsFromDatabase()).isEmpty();
		;

	}

	@Test
	void testUpdate() throws SQLException {
		stmt.executeUpdate("insert into libretto values (" + "'B027500', " + "'Data Mining and Organization', " + "12, "
				+ "'28', " + "'2020-01-29')");

		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
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
		return new Exam(rs.getString("id"), rs.getString("description"), rs.getInt("weight"),
				new Grade(rs.getString("grade")), LocalDate.parse(rs.getString("date")));
	}

}
