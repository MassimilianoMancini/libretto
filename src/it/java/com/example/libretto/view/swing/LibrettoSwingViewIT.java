package com.example.libretto.view.swing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

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
	public void tearDownDB() throws SQLException {
		stmt.close();
		conn.close();
		
	}

	@Override
	protected void onSetUp() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onTearDown() throws ManagedProcessException {
		db.stop();
		
	}
	

}
