package com.example.libretto.app.swing;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.libretto.Generated;
import com.example.libretto.controller.LibrettoController;
import com.example.libretto.repository.mariadb.ExamMariaDBRepository;
import com.example.libretto.view.swing.LibrettoSwingView;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Generated
@Command(mixinStandardHelpOptions = true)
public class LibrettoSwingApp implements Callable<Void> {
	
	@Option(names = { "--host" }, description = "MariaDB host address")
	private String host = "localhost";
	
	@Option(names = { "--port" }, description = "MariaDB host port")
	private int port = 3306;
	
	@Option(names = { "--user" }, description = "user")
	private String user = "root";
	
	@Option(names = { "--password" }, description = "password", arity = "0..1", required = true, interactive = true)
	private String password;
	
	@Option(names = { "--db" }, description = "MariaDB database name")
	private String databaseName = "libretto";
	
	public static void main(String[] args) {
		new CommandLine(new LibrettoSwingApp()).execute(args);
	}

	@Override
	public Void call() {
		Connection conn = null;
		
		for (int i = 1; (conn == null) && (i < 60); i++) {
			
			try {
				conn = DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port, user, password);
			} catch (SQLException e) {
				System.out.println("Attempt to connect to DB n. " + i);
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "Unable to connect to DB", e);
			}
		
			if (conn != null) {
				if (i > 1) {
					System.out.println("Connected!");
				}
				
				createLibrettoTable(conn);
				runSwingView(conn);
			}
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Interruzione", e);
				Thread.currentThread().interrupt();
			}
		}
		return null;
	}

	private void runSwingView(Connection conn) {
		EventQueue.invokeLater( () -> {
			ExamMariaDBRepository examRepository = new ExamMariaDBRepository(conn);
			LibrettoSwingView librettoView = new LibrettoSwingView();
			LibrettoController librettoController = new LibrettoController(librettoView, examRepository);
			librettoView.setLibrettoController(librettoController);
			librettoView.setVisible(true);
			librettoController.allExams();
		});
	}

	private void createLibrettoTable(Connection conn) {
		String query;
			
		query = "drop database if exists " + databaseName + "; ";
		query += "create database " + databaseName + "; ";
		query += "use " + databaseName + "; ";
		query += "create table libretto " +
				"(id varchar(7) not null primary key, " + 
				"description varchar(60) not null, " + 
				"weight int not null, " + 
				"grade varchar(3) not null, " + 
				"date date not null);";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.executeQuery();
		}   catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Errore durante la creazione della tabella libretto", e);
		}
	}

}
