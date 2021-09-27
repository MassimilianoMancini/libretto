package com.example.libretto.view.swing;

import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.example.libretto.Generated;
import com.example.libretto.controller.LibrettoController;
import com.example.libretto.model.Averages;
import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;
import com.example.libretto.view.LibrettoView;

public class LibrettoSwingView extends JFrame implements LibrettoView {

	private static final String DATE_FORMAT_IT = "dd-MM-yyyy";
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtDescription;
	private JTextField txtWeight;
	private JComboBox<String> cmbGrade;
	private JTextField txtDate;
	private JList<Exam> lstExam;
	private DefaultListModel<Exam> lstExamModel;
	private JButton btnSave;
	private JButton btnDelete;
	private List<JTextField> fieldList = new ArrayList<>();
	private JScrollPane scrollPane;
	private JTextField txtAverage;
	private JTextField txtWeightedAverage;
	private JLabel lblErrorMessage;

	private transient LibrettoController librettoController;

	public LibrettoSwingView() {
		setTitle("Libretto universitario");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 520);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[] { 120, 70, 10, 50, 10, 70, 50, 90, 170 };
		gblContentPane.rowHeights = new int[] { 0, 354, 0, 0, 0, 0, 0 };
		contentPane.setLayout(gblContentPane);

		String lblListHeaderFormat = String.format("%7s %-60s %4s %4s %-10s", "Codice", "Descrizione", "Peso", "Voto", "Data");

		// Header
		JLabel lblListHeader = new JLabel(lblListHeaderFormat);
		lblListHeader.setFont(new Font("monospaced", Font.BOLD, 12));
		lblListHeader.setName("lblListHeader");
		GridBagConstraints gbcLblListHeader = new GridBagConstraints();
		gbcLblListHeader.gridwidth = 9;
		gbcLblListHeader.fill = GridBagConstraints.HORIZONTAL;
		gbcLblListHeader.insets = new Insets(0, 0, 0, 5);
		gbcLblListHeader.gridx = 0;
		gbcLblListHeader.gridy = 0;
		getContentPane().add(lblListHeader, gbcLblListHeader);

		// Main pane
		scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.gridwidth = 9;
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 1;
		contentPane.add(scrollPane, gbcScrollPane);

		lstExamModel = new DefaultListModel<>();
		lstExam = new JList<>(lstExamModel);
		lstExam.setName("lstExam");
		lstExam.addListSelectionListener(e -> btnDelete.setEnabled(lstExam.getSelectedIndex() != -1));
		lstExam.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Exam exam = (Exam) value;
				return super.getListCellRendererComponent(list, getDisplayListString(exam), index, isSelected,
						cellHasFocus);
			}
		});
		lstExam.setFont(new Font("monospaced", Font.PLAIN, 12));
		lstExam.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setViewportView(lstExam);

		// Average
		JLabel lblAverage = new JLabel("Media");
		lblAverage.setName("lblAverage");
		GridBagConstraints gbcLblAverage = new GridBagConstraints();
		gbcLblAverage.anchor = GridBagConstraints.EAST;
		gbcLblAverage.insets = new Insets(0, 0, 0, 5);
		gbcLblAverage.gridx = 0;
		gbcLblAverage.gridy = 2;
		getContentPane().add(lblAverage, gbcLblAverage);

		txtAverage = new JTextField();
		txtAverage.setName("txtAverage");
		txtAverage.setText(" ");
		txtAverage.setEditable(false);
		GridBagConstraints gbcTxtAverage = new GridBagConstraints();
		gbcTxtAverage.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtAverage.insets = new Insets(0, 0, 0, 5);
		gbcTxtAverage.gridx = 1;
		gbcTxtAverage.gridy = 2;
		getContentPane().add(txtAverage, gbcTxtAverage);

		// Weighted average
		JLabel lblWeightedAverage = new JLabel("Media pesata");
		lblWeightedAverage.setName("lblWeightedAverage");
		GridBagConstraints gbcLblWeightedAverage = new GridBagConstraints();
		gbcLblWeightedAverage.anchor = GridBagConstraints.EAST;
		gbcLblWeightedAverage.insets = new Insets(0, 0, 5, 5);
		gbcLblWeightedAverage.gridx = 6;
		gbcLblWeightedAverage.gridy = 2;
		getContentPane().add(lblWeightedAverage, gbcLblWeightedAverage);

		txtWeightedAverage = new JTextField();
		txtWeightedAverage.setName("txtWeightedAverage");
		txtWeightedAverage.setText(" ");
		txtWeightedAverage.setEditable(false);
		GridBagConstraints gbcTxtWeightedAverage = new GridBagConstraints();
		gbcTxtWeightedAverage.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtWeightedAverage.insets = new Insets(0, 0, 5, 5);
		gbcTxtWeightedAverage.gridx = 7;
		gbcTxtWeightedAverage.gridy = 2;
		getContentPane().add(txtWeightedAverage, gbcTxtWeightedAverage);

		// Delete button
		btnDelete = new JButton("Elimina");
		btnDelete.setEnabled(false);
		btnDelete.setName("btnDelete");
		btnDelete.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(this, "Confermi la cancellazione", "Cancellazione",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				librettoController.deleteExam(lstExam.getSelectedValue());
			}
		});

		GridBagConstraints gbcBtnDelete = new GridBagConstraints();
		gbcBtnDelete.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnDelete.gridx = 8;
		gbcBtnDelete.gridy = 2;
		getContentPane().add(btnDelete, gbcBtnDelete);

		// Error messages
		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("lblErrorMessage");
		lblErrorMessage.setForeground(Color.RED);
		GridBagConstraints gbcLblErrorMessage = new GridBagConstraints();
		gbcLblErrorMessage.fill = GridBagConstraints.HORIZONTAL;
		gbcLblErrorMessage.gridwidth = 8;
		gbcLblErrorMessage.insets = new Insets(0, 0, 5, 5);
		gbcLblErrorMessage.gridx = 1;
		gbcLblErrorMessage.gridy = 3;
		getContentPane().add(lblErrorMessage, gbcLblErrorMessage);

		// Exam description
		JLabel lblDescription = new JLabel("Descrizione");
		lblDescription.setName("lblDescription");
		GridBagConstraints gbcLblDescription = new GridBagConstraints();
		gbcLblDescription.anchor = GridBagConstraints.EAST;
		gbcLblDescription.insets = new Insets(0, 0, 5, 5);
		gbcLblDescription.gridx = 0;
		gbcLblDescription.gridy = 4;
		getContentPane().add(lblDescription, gbcLblDescription);

		txtDescription = new JTextField();
		txtDescription.setName("txtDescription");
		GridBagConstraints gbcTxtDescription = new GridBagConstraints();
		gbcTxtDescription.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtDescription.gridwidth = 8;
		gbcTxtDescription.insets = new Insets(0, 0, 5, 0);
		gbcTxtDescription.gridx = 1;
		gbcTxtDescription.gridy = 4;
		getContentPane().add(txtDescription, gbcTxtDescription);
		txtDescription.setColumns(10);
		fieldList.add(txtDescription);

		// Exam ID
		JLabel lblId = new JLabel("Codice");
		lblId.setName("lblId");
		GridBagConstraints gbcLblId = new GridBagConstraints();
		gbcLblId.anchor = GridBagConstraints.EAST;
		gbcLblId.insets = new Insets(0, 0, 0, 5);
		gbcLblId.gridx = 0;
		gbcLblId.gridy = 5;
		getContentPane().add(lblId, gbcLblId);

		txtId = new JTextField();
		txtId.setName("txtId");
		GridBagConstraints gbcTxtId = new GridBagConstraints();
		gbcTxtId.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtId.insets = new Insets(0, 0, 0, 5);
		gbcTxtId.gridx = 1;
		gbcTxtId.gridy = 5;
		getContentPane().add(txtId, gbcTxtId);
		fieldList.add(txtId);

		// Exam weight
		JLabel lblWeight = new JLabel("Peso");
		lblWeight.setName("lblWeight");
		GridBagConstraints gbcLblWeight = new GridBagConstraints();
		gbcLblWeight.anchor = GridBagConstraints.EAST;
		gbcLblWeight.insets = new Insets(0, 0, 0, 5);
		gbcLblWeight.gridx = 2;
		gbcLblWeight.gridy = 5;
		getContentPane().add(lblWeight, gbcLblWeight);

		txtWeight = new JTextField();
		txtWeight.setName("txtWeight");
		GridBagConstraints gbcTxtWeight = new GridBagConstraints();
		gbcTxtWeight.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtWeight.insets = new Insets(0, 0, 0, 5);
		gbcTxtWeight.gridx = 3;
		gbcTxtWeight.gridy = 5;
		getContentPane().add(txtWeight, gbcTxtWeight);
		fieldList.add(txtWeight);

		// Exam combo grade
		JLabel lblGrade = new JLabel("Voto");
		lblGrade.setName("lblGrade");
		GridBagConstraints gbcLblGrade = new GridBagConstraints();
		gbcLblGrade.anchor = GridBagConstraints.EAST;
		gbcLblGrade.insets = new Insets(0, 0, 0, 5);
		gbcLblGrade.gridx = 4;
		gbcLblGrade.gridy = 5;
		getContentPane().add(lblGrade, gbcLblGrade);

		cmbGrade = new JComboBox<>();
		cmbGrade.setName("cmbGrade");
		cmbGrade.setModel(new DefaultComboBoxModel<>(new String[] { "", "18", "19", "20", "21", "22", "23", "24", "25",
				"26", "27", "28", "29", "30", "30L" }));
		GridBagConstraints gbcCmbGrade = new GridBagConstraints();
		gbcCmbGrade.fill = GridBagConstraints.HORIZONTAL;
		gbcCmbGrade.anchor = GridBagConstraints.NORTH;
		gbcCmbGrade.insets = new Insets(0, 0, 0, 5);
		gbcCmbGrade.gridx = 5;
		gbcCmbGrade.gridy = 5;
		getContentPane().add(cmbGrade, gbcCmbGrade);

		// Exam date
		JLabel lblDate = new JLabel("Data");
		lblDate.setName("lblDate");
		GridBagConstraints gbcLblDate = new GridBagConstraints();
		gbcLblDate.anchor = GridBagConstraints.EAST;
		gbcLblDate.insets = new Insets(0, 0, 0, 5);
		gbcLblDate.gridx = 6;
		gbcLblDate.gridy = 5;
		getContentPane().add(lblDate, gbcLblDate);

		txtDate = new JTextField();
		txtDate.setName("txtDate");
		txtDate.setText(new SimpleDateFormat(DATE_FORMAT_IT).format(new Date()));
		GridBagConstraints gbcTxtDate = new GridBagConstraints();
		gbcTxtDate.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtDate.insets = new Insets(0, 0, 0, 5);
		gbcTxtDate.gridx = 7;
		gbcTxtDate.gridy = 5;
		getContentPane().add(txtDate, gbcTxtDate);
		fieldList.add(txtDate);

		// Save button
		btnSave = new JButton("Salva");
		btnSave.setEnabled(false);
		btnSave.setName("btnSave");
		btnSave.addActionListener(e -> {
			Exam exam = null;
			LocalDate ld = getDateInLocalDate(txtDate.getText());
			if (ld != null) {
				try {
				exam = new Exam(
					txtId.getText(), 
					txtDescription.getText(),
					Integer.parseInt(txtWeight.getText()), 
					new Grade((String) cmbGrade.getSelectedItem()), 
					ld);
				librettoController.newExam(exam);
				} catch (IllegalArgumentException e1) {
					showError(e1.getMessage());
				}			
			} else {
				txtDate.setText("");
			}
		});
		GridBagConstraints gbcBtnSave = new GridBagConstraints();
		gbcBtnSave.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnSave.anchor = GridBagConstraints.NORTH;
		gbcBtnSave.gridx = 8;
		gbcBtnSave.gridy = 5;
		getContentPane().add(btnSave, gbcBtnSave);

		// Add a document listener
		DocumentListener listener = new DocumentListener() {

			@Generated
			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				checkFieldsNotEmpty();
			}

		};

		// Attach documentListener to all text fields and to combo
		for (JTextField tf : fieldList) {
			tf.getDocument().addDocumentListener(listener);
		}

		cmbGrade.addActionListener(e -> checkFieldsNotEmpty());

	}

	@Override
	public void showAllExams(List<Exam> exams) {
		exams.stream().forEach(lstExamModel::addElement);
		updateAverages(exams);
	}

	@Override
	public void examAdded(Exam exam) {
		lstExamModel.addElement(exam);
		lblErrorMessage.setText(" ");
		updateAverages(getListOfExams());
		resetFields();
	}

	@Override
	public void showError(String message) {
		lblErrorMessage.setText(message);
	}

	@Override
	public void examRemoved(Exam exam) {
		lstExamModel.removeElement(exam);
		lblErrorMessage.setText(" ");
		updateAverages(getListOfExams());

	}

	@Override
	public void showErrorExamNotFound(String message, Exam exam) {
		lblErrorMessage.setText(message + ": " + getDisplayErrorString(exam));
		lstExamModel.removeElement(exam);
	}
	
	@Override
	public void showErrorExamAlreadyExists(String message, Exam exam) {
		lblErrorMessage.setText(message + ": " + getDisplayErrorString(exam));
		boolean exists = false;
		for (int i = 0; i < lstExamModel.size(); i++) {
			if (exam.getId().equals(lstExamModel.get(i).getId())) {
				exists = true;
			}
		}	
		if (!exists) {
			lstExamModel.addElement(exam);
		}
	}
	
	public void setLibrettoController(LibrettoController librettoController) {
		this.librettoController = librettoController;
	}
	
	DefaultListModel<Exam> getLstExamModel() {
		return lstExamModel;
	}
	
	private List<Exam> getListOfExams() {
		ArrayList<Exam> concreteList = new ArrayList<>();
		for (int i = 0; i < lstExamModel.getSize(); i++) {
			concreteList.add(lstExamModel.get(i));
		}
		return concreteList;
	}

	private void updateAverages(List<Exam> exams) {
		txtAverage.setText(String.format(Locale.ROOT, "%.02f", new Averages(exams).getAverage()));
		txtWeightedAverage.setText(String.format(Locale.ROOT, "%.02f", new Averages(exams).getWeightedAverage()));
	}

	private LocalDate getDateInLocalDate(String date) {
		LocalDate ld = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_IT);
			ld = LocalDate.parse(date, formatter);
		} catch (DateTimeParseException e) {
			lblErrorMessage.setText("Il formato data è gg-mm-aaaa");
		}
		return ld;
	}

	private void checkFieldsNotEmpty() {
		boolean canEnable = true;
		for (JTextField tf : fieldList) {
			if (tf.getText().isEmpty()) {
				canEnable = false;
				break;
			}
		}

		canEnable = canEnable && cmbGrade.getSelectedIndex() != -1;
		btnSave.setEnabled(canEnable);
	}

	private void resetFields() {
		for (JTextField tf : fieldList) {
			tf.setText("");
		}
		cmbGrade.setSelectedIndex(-1);
		txtDate.setText(new SimpleDateFormat(DATE_FORMAT_IT).format(new Date()));
	}

	private String getDisplayListString(Exam exam) {
		return String.format("%-7s|%-60s|%4d|%4s|%10s", exam.getId(), exam.getDescription(), exam.getWeight(),
				exam.getGrade().getValue(), exam.getDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT_IT)));
	}

	private String getDisplayErrorString(Exam exam) {
		return String.format("%-7s - %-40s (%2d) %3s %10s", exam.getId(), exam.getDescription(), exam.getWeight(),
				exam.getGrade().getValue(), exam.getDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT_IT)));
	}

}
