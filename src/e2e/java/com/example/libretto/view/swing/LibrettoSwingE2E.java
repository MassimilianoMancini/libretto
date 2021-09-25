package com.example.libretto.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.swing.timing.Timeout.timeout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

//If run in Eclipse, an up and running MariDB server is needed
//for example via 'docker run --rm -p 3306:3306 -e MARIADB_ROOT_PASSWORD=password mariadb:10.6.4'


@RunWith(GUITestRunner.class)
public class LibrettoSwingE2E extends AssertJSwingJUnitTestCase {
	
	private static final String LIBRETTO_DB_NAME = "librettotest";
	private static Connection conn = null;
	private static int mariadbPort = Integer.parseInt(System.getProperty("mariadb.port", "3306"));
	private static String mariadbPassword = System.getProperty("MARIADB_ROOT_PASSWORD", "password");
	private Statement stmt;
	private FrameFixture window;
	
	@BeforeClass
	public static void establishConnection() throws InterruptedException {
		for (int i = 1; (conn == null) && (i < 60); i++) {
			try {
				conn = DriverManager.getConnection("jdbc:mariadb://localhost:" + mariadbPort, "root", mariadbPassword);
			} catch (SQLException e) {
				System.out.println("[ExamMariaDBRepositoryIT] DB connection attempt # " + i);
				TimeUnit.SECONDS.sleep(1);
			}
		}
	}
	

	@Override
	protected void onSetUp() throws Exception {
		stmt = conn.createStatement();
		stmt.executeUpdate("drop database if exists " + LIBRETTO_DB_NAME);
		stmt.executeUpdate("create database " + LIBRETTO_DB_NAME);
		stmt.executeUpdate("use " + LIBRETTO_DB_NAME);
		stmt.executeUpdate(
				"create table libretto (" + "id varchar(7) not null primary key, " + "description varchar(60) not null,"
						+ "weight int not null," + "grade varchar(3) not null," + "date date not null" + ")");
		stmt.executeUpdate("insert into libretto values (" + "'B027500', " + "'Data Mining and Organization', " + "12, "
				+ "'30L', " + "'2020-01-29')");

		stmt.executeUpdate("insert into libretto values (" + "'B027507', " + "'Parallel Computing', " + "6, " + "'27', "
				+ "'2020-01-09')");
		application("com.example.libretto.app.swing.LibrettoSwingApp")
			.withArgs(
				"--host=localhost", 
				"--port=" + mariadbPort,
				"--user=root",
				"--password=" + mariadbPassword,
				"--db=" + LIBRETTO_DB_NAME)
			.start();
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Libretto universitario".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}
	
	@AfterClass
	public static void closeConnection() throws SQLException {
		conn.close();
	}
	
	@Test
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list().contents())
			.anySatisfy(e -> assertThat(e).contains(
				"B027500", 
				"Data Mining and Organization",
				"12",
				"30L",
				"2020-01-29"))
			.anySatisfy(e -> assertThat(e).contains(
				"B027507",
				"Parallel Computing",
				"6",
				"27",
				"2020-01-09"));
	}
	
	@Test
	public void testSaveButtonSuccess() {
		window.textBox("txtId").setText("B027536");
		window.textBox("txtDescription").setText("Numerical Methods for Graphics");
		window.textBox("txtWeight").setText("6");
		window.comboBox("cmbGrade").selectItem(9);
		window.textBox("txtDate").setText("15-06-2020");
		window.button("btnSave").click();
		assertThat(window.list().contents())
		.anySatisfy(e -> assertThat(e).contains(
			"B027536",
			"Numerical Methods for Graphics",
			"6",
			"26",
			"2020-06-15"));
	}
	
	@Test
	public void testSaveButtonError() {
		window.textBox("txtId").setText("B027500");
		window.textBox("txtDescription").setText("Another fake exam");
		window.textBox("txtWeight").setText("6");
		window.comboBox("cmbGrade").selectItem(9);
		window.textBox("txtDate").setText("15-06-2020");
		window.button("btnSave").click();
		assertThat(window.label("lblErrorMessage").text()).contains("B027500", "Data Mining and Organization");
	}
	
	@Test
	public void testDeleteButtonSuccess() {
		window.list().selectItem(Pattern.compile(".*B027500.*"));
		window.button("btnDelete").click();
		window.optionPane(timeout(10000)).yesButton().click();
		assertThat(window.list().contents()).noneMatch(e -> e.contains("B027500"));
	}
	
	@Test
	public void testDeleteButtonError() throws SQLException {
		window.list().selectItem(Pattern.compile(".*B027500.*"));
		deleteExamFromDB("B027500");
		window.button("btnDelete").click();
		window.optionPane(timeout(10000)).yesButton().click();
		assertThat(window.label("lblErrorMessage").text()).contains("B027500", "Data Mining and Organization");
	}
	
	private void deleteExamFromDB(String id) throws SQLException {
		String query = "delete from libretto where id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, id);
			pstmt.executeQuery();
		}
		
	}
}
