package com.example.libretto.view.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[] { 20, 70, 10, 50, 10, 70, 50, 90, 70 };
		gblContentPane.rowHeights = new int[] { 354, 0, 0, 0, 0, 0 };
		contentPane.setLayout(gblContentPane);

		scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.gridwidth = 9;
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 0;
		contentPane.add(scrollPane, gbcScrollPane);

		lstExamModel = new DefaultListModel<>();
		lstExam = new JList<>(lstExamModel);
		lstExam.setName("lstExam");
		lstExam.addListSelectionListener(e -> btnDelete.setEnabled(lstExam.getSelectedIndex() != -1));
		scrollPane.setViewportView(lstExam);

		JLabel lblAverage = new JLabel("Media");
		lblAverage.setName("lblAverage");
		GridBagConstraints gbcLblAverage = new GridBagConstraints();
		gbcLblAverage.fill = GridBagConstraints.HORIZONTAL;
		gbcLblAverage.insets = new Insets(0, 0, 5, 5);
		gbcLblAverage.gridx = 0;
		gbcLblAverage.gridy = 1;
		getContentPane().add(lblAverage, gbcLblAverage);

		txtAverage = new JTextField();
		txtAverage.setName("txtAverage");
		txtAverage.setText("0.0");
		txtAverage.setEditable(false);
		GridBagConstraints gbcTxtAverage = new GridBagConstraints();
		gbcTxtAverage.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtAverage.insets = new Insets(0, 0, 5, 5);
		gbcTxtAverage.gridx = 1;
		gbcTxtAverage.gridy = 1;
		getContentPane().add(txtAverage, gbcTxtAverage);

		JLabel lblWeightedAverage = new JLabel("Media pesata");
		lblWeightedAverage.setName("lblWeightedAverage");
		GridBagConstraints gbcLblWeightedAverage = new GridBagConstraints();
		gbcLblWeightedAverage.fill = GridBagConstraints.HORIZONTAL;
		gbcLblWeightedAverage.insets = new Insets(0, 0, 5, 5);
		gbcLblWeightedAverage.gridx = 2;
		gbcLblWeightedAverage.gridy = 1;
		getContentPane().add(lblWeightedAverage, gbcLblWeightedAverage);

		txtWeightedAverage = new JTextField();
		txtWeightedAverage.setName("txtWeightedAverage");
		txtWeightedAverage.setText("0.0");
		txtWeightedAverage.setEditable(false);
		GridBagConstraints gbcTxtWeightedAverage = new GridBagConstraints();
		gbcTxtWeightedAverage.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtWeightedAverage.insets = new Insets(0, 0, 5, 5);
		gbcTxtWeightedAverage.gridx = 3;
		gbcTxtWeightedAverage.gridy = 1;
		getContentPane().add(txtWeightedAverage, gbcTxtWeightedAverage);

		btnDelete = new JButton("Elimina");
		btnDelete.setEnabled(false);
		btnDelete.setName("btnDelete");
		btnDelete.addActionListener(e -> librettoController.deleteExam(lstExam.getSelectedValue()));

		GridBagConstraints gbcBtnDelete = new GridBagConstraints();
		gbcBtnDelete.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnDelete.gridx = 8;
		gbcBtnDelete.gridy = 1;
		getContentPane().add(btnDelete, gbcBtnDelete);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("lblErrorMessage");
		GridBagConstraints gbcLblErrorMessage = new GridBagConstraints();
		gbcLblErrorMessage.fill = GridBagConstraints.HORIZONTAL;
		gbcLblErrorMessage.gridwidth = 9;
		gbcLblErrorMessage.insets = new Insets(0, 0, 5, 5);
		gbcLblErrorMessage.gridx = 0;
		gbcLblErrorMessage.gridy = 2;
		getContentPane().add(lblErrorMessage, gbcLblErrorMessage);

		JLabel lblDescription = new JLabel("Descrizione");
		lblDescription.setName("lblDescription");
		GridBagConstraints gbcLblDescription = new GridBagConstraints();
		gbcLblDescription.anchor = GridBagConstraints.WEST;
		gbcLblDescription.insets = new Insets(0, 0, 5, 5);
		gbcLblDescription.gridx = 0;
		gbcLblDescription.gridy = 3;
		getContentPane().add(lblDescription, gbcLblDescription);

		txtDescription = new JTextField();
		txtDescription.setName("txtDescription");
		GridBagConstraints gbcTxtDescription = new GridBagConstraints();
		gbcTxtDescription.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtDescription.gridwidth = 8;
		gbcTxtDescription.insets = new Insets(0, 0, 5, 0);
		gbcTxtDescription.gridx = 1;
		gbcTxtDescription.gridy = 3;
		getContentPane().add(txtDescription, gbcTxtDescription);
		txtDescription.setColumns(10);
		fieldList.add(txtDescription);

		JLabel lblId = new JLabel("Codice");
		lblId.setName("lblId");
		GridBagConstraints gbcLblId = new GridBagConstraints();
		gbcLblId.anchor = GridBagConstraints.EAST;
		gbcLblId.insets = new Insets(0, 0, 0, 5);
		gbcLblId.gridx = 0;
		gbcLblId.gridy = 4;
		getContentPane().add(lblId, gbcLblId);

		txtId = new JTextField();
		txtId.setName("txtId");
		GridBagConstraints gbcTxtId = new GridBagConstraints();
		gbcTxtId.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtId.insets = new Insets(0, 0, 0, 5);
		gbcTxtId.gridx = 1;
		gbcTxtId.gridy = 4;
		getContentPane().add(txtId, gbcTxtId);
		fieldList.add(txtId);

		JLabel lblWeight = new JLabel("Peso");
		lblWeight.setName("lblWeight");
		GridBagConstraints gbcLblWeight = new GridBagConstraints();
		gbcLblWeight.anchor = GridBagConstraints.EAST;
		gbcLblWeight.insets = new Insets(0, 0, 0, 5);
		gbcLblWeight.gridx = 2;
		gbcLblWeight.gridy = 4;
		getContentPane().add(lblWeight, gbcLblWeight);

		txtWeight = new JTextField();
		txtWeight.setName("txtWeight");
		GridBagConstraints gbcTxtWeight = new GridBagConstraints();
		gbcTxtWeight.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtWeight.insets = new Insets(0, 0, 0, 5);
		gbcTxtWeight.gridx = 3;
		gbcTxtWeight.gridy = 4;
		getContentPane().add(txtWeight, gbcTxtWeight);
		fieldList.add(txtWeight);

		JLabel lblGrade = new JLabel("Voto");
		lblGrade.setName("lblGrade");
		GridBagConstraints gbcLblGrade = new GridBagConstraints();
		gbcLblGrade.anchor = GridBagConstraints.EAST;
		gbcLblGrade.insets = new Insets(0, 0, 0, 5);
		gbcLblGrade.gridx = 4;
		gbcLblGrade.gridy = 4;
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
		gbcCmbGrade.gridy = 4;
		getContentPane().add(cmbGrade, gbcCmbGrade);

		JLabel lblDate = new JLabel("Data");
		lblDate.setName("lblDate");
		GridBagConstraints gbcLblDate = new GridBagConstraints();
		gbcLblDate.anchor = GridBagConstraints.EAST;
		gbcLblDate.insets = new Insets(0, 0, 0, 5);
		gbcLblDate.gridx = 6;
		gbcLblDate.gridy = 4;
		getContentPane().add(lblDate, gbcLblDate);

		txtDate = new JTextField();
		txtDate.setName("txtDate");
		GridBagConstraints gbcTxtDate = new GridBagConstraints();
		gbcTxtDate.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtDate.insets = new Insets(0, 0, 0, 5);
		gbcTxtDate.gridx = 7;
		gbcTxtDate.gridy = 4;
		getContentPane().add(txtDate, gbcTxtDate);
		fieldList.add(txtDate);

		btnSave = new JButton("Salva");
		btnSave.setEnabled(false);
		btnSave.setName("btnSave");
		btnSave.addActionListener(e -> librettoController
				.newExam(new Exam(txtId.getText(), txtDescription.getText(), Integer.parseInt(txtWeight.getText()),
						new Grade((String) cmbGrade.getSelectedItem()), getDateInLocalDate(txtDate.getText()))));
		GridBagConstraints gbcBtnSave = new GridBagConstraints();
		gbcBtnSave.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnSave.anchor = GridBagConstraints.NORTH;
		gbcBtnSave.gridx = 8;
		gbcBtnSave.gridy = 4;
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

	DefaultListModel<Exam> getLstExamModel() {
		return lstExamModel;
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
	}

	@Override
	public void showError(String message, Exam exam) {
		lblErrorMessage.setText(message + ": " + exam);
	}

	@Override
	public void examRemoved(Exam exam) {
		lstExamModel.removeElement(exam);
		lblErrorMessage.setText(" ");
		updateAverages(getListOfExams());

	}

	private List<Exam> getListOfExams() {
		ArrayList<Exam> concreteList = new ArrayList<>();
		for (int i = 0; i < lstExamModel.getSize(); i++) {
			concreteList.add(lstExamModel.get(i));
		}
		return concreteList;
	}

	private void updateAverages(List<Exam> exams) {
		txtAverage.setText((new Averages(exams).getAverage()).toString());
		txtWeightedAverage.setText((new Averages(exams).getWeightedAverage()).toString());
	}

	private LocalDate getDateInLocalDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return LocalDate.parse(date, formatter);
	}

	public void setLibrettoController(LibrettoController librettoController) {
		this.librettoController = librettoController;
	}

	private void checkFieldsNotEmpty() {
		boolean canEnable = true;
		for (JTextField tf : fieldList) {
			if (tf.getText().isEmpty()) {
				canEnable = false;
				break;
			}
		}

		canEnable = canEnable && cmbGrade.getSelectedIndex() != 0;

		btnSave.setEnabled(canEnable);
	}

	@Override
	public void showErrorExamNotFound(String message, Exam exam) {
		lblErrorMessage.setText(message + ": " + exam);
		lstExamModel.removeElement(exam);
	}

	@Override
	public void showErrorExamAlreadyExists(String message, Exam exam) {
		lblErrorMessage.setText(message + ": " + exam);
		lstExamModel.addElement(exam);
	}
}
