package com.example.libretto.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.regex.Pattern;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.libretto.controller.LibrettoController;
import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;
import com.example.libretto.repository.mariadb.ExamMariaDBRepository;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

public class LibrettoSwingViewIT extends AssertJSwingJUnitTestCase {
	
	private static final String LIBRETTO_DB_NAME = "libretto";
	private static DB db;
	private static DBConfigurationBuilder config;
	private Connection conn;
	private Statement stmt;
	private ExamMariaDBRepository examRepository;
	private LibrettoSwingView librettoSwingView;
	private LibrettoController librettoController;
	private FrameFixture window;
	
	@BeforeClass
	public static void setupServer() throws ManagedProcessException {

		config = DBConfigurationBuilder.newBuilder();
		config.setPort(0);
		
		db = DB.newEmbeddedDB(config.build());
		db.start();
	}
	
	@Override
	protected void onSetUp() throws Exception {
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
		
		GuiActionRunner.execute(() -> {
			librettoSwingView = new LibrettoSwingView();
			librettoController = new LibrettoController(librettoSwingView, examRepository);
			librettoSwingView.setLibrettoController(librettoController);
			return librettoSwingView;
		});
		window = new FrameFixture(robot(), librettoSwingView);
		window.show();
	}
	
	@Override
	protected void onTearDown() throws ManagedProcessException, SQLException {
		stmt.close();
		conn.close();
	}
	
	@AfterClass
	public static void shutdownServer() throws ManagedProcessException {
		db.stop();
	}
	
	@Test
	public void testAllExams() throws SQLException {
		Exam exam1 = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"), LocalDate.of(2020, 1, 29));
		Exam exam2 = new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		examRepository.save(exam1);
		examRepository.save(exam2);
		GuiActionRunner.execute(() -> librettoController.allExams());
		assertThat(window.list().contents()).containsExactly(exam1.toString(), exam2.toString());	
	}
	
	@Test
	public void testAddButtonSuccess() {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").setText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2020");
		window.button("btnSave").click();
		assertThat(window.list().contents()).containsExactly(new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)).toString());
		window.textBox("txtId").requireEmpty();
		window.textBox("txtDescription").requireEmpty();
		window.textBox("txtWeight").requireEmpty();
		window.comboBox("cmbGrade").requireNoSelection();
		window.textBox("txtDate").requireText(Pattern.compile("^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$"));
	}
	
	@Test
	public void testAddButtonError() throws SQLException {
		Exam exam = new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		examRepository.save(exam);
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").setText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2020");
		window.button("btnSave").click();
		assertThat(window.list().contents()).containsExactly(exam.toString());
		window.label("lblErrorMessage").requireText("Esame già presente con codice B027507: " + exam);
	}
	
	@Test
	public void testDeleteButtonSuccess() {
		Exam exam = new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		GuiActionRunner.execute(() -> librettoController.newExam(exam));
		window.list().selectItem(0);
		window.button("btnDelete").click();
		window.optionPane().yesButton().click();
		assertThat(window.list().contents()).isEmpty();
	}
	
	@Test
	public void testDeleteButtonError() {
		Exam exam = new Exam("B027000", "Fake Exam", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		GuiActionRunner.execute(() -> librettoSwingView.getLstExamModel().addElement(exam));
		window.list().selectItem(0);
		window.button("btnDelete").click();
		window.optionPane().yesButton().click();
		assertThat(window.list().contents()).isEmpty();
		window.label("lblErrorMessage").requireText("Esame inesistente con codice B027000: " + exam);
	}
	
	@Test
	public void testDeleteAndCancelNothingHappens() {
		Exam exam = new Exam("B027000", "Fake Exam", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		GuiActionRunner.execute(() -> librettoSwingView.getLstExamModel().addElement(exam));
		window.list().selectItem(0);
		window.button("btnDelete").click();
		window.optionPane().noButton().click();
		assertThat(window.list().contents()).containsExactly(exam.toString());
	}
	

}
