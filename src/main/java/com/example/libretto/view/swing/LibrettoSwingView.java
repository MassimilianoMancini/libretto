package com.example.libretto.view.swing;

import java.util.List;

import javax.swing.JFrame;

import com.example.libretto.model.Exam;
import com.example.libretto.view.LibrettoView;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;

public class LibrettoSwingView extends JFrame implements LibrettoView {
	public LibrettoSwingView() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 50, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblId = new JLabel("Id");
		lblId.setName("lblId");
		GridBagConstraints gbcLblId = new GridBagConstraints();
		gbcLblId.insets = new Insets(0, 0, 5, 5);
		gbcLblId.anchor = GridBagConstraints.EAST;
		gbcLblId.gridx = 0;
		gbcLblId.gridy = 0;
		getContentPane().add(lblId, gbcLblId);
		
		txtId = new JTextField();
		txtId.setName("txtId");
		GridBagConstraints gbcTxtId = new GridBagConstraints();
		gbcTxtId.anchor = GridBagConstraints.WEST;
		gbcTxtId.insets = new Insets(0, 0, 5, 5);
		gbcTxtId.gridx = 1;
		gbcTxtId.gridy = 0;
		getContentPane().add(txtId, gbcTxtId);
		txtId.setColumns(10);
		
		JLabel lblDescription = new JLabel("Descrizione");
		lblDescription.setName("lblDescription");
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.EAST;
		gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescription.gridx = 2;
		gbc_lblDescription.gridy = 0;
		getContentPane().add(lblDescription, gbc_lblDescription);
		
		txtDescription = new JTextField();
		txtDescription.setName("txtDescription");
		GridBagConstraints gbc_txtDescription = new GridBagConstraints();
		gbc_txtDescription.gridwidth = 3;
		gbc_txtDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDescription.insets = new Insets(0, 0, 5, 5);
		gbc_txtDescription.gridx = 3;
		gbc_txtDescription.gridy = 0;
		getContentPane().add(txtDescription, gbc_txtDescription);
		txtDescription.setColumns(10);
		
		JLabel lblWeight = new JLabel("Peso");
		lblWeight.setName("lblWeight");
		GridBagConstraints gbc_lblWeight = new GridBagConstraints();
		gbc_lblWeight.anchor = GridBagConstraints.EAST;
		gbc_lblWeight.insets = new Insets(0, 0, 0, 5);
		gbc_lblWeight.gridx = 0;
		gbc_lblWeight.gridy = 1;
		getContentPane().add(lblWeight, gbc_lblWeight);
		
		txtWeight = new JTextField();
		txtWeight.setName("txtWeight");
		GridBagConstraints gbc_txtWeight = new GridBagConstraints();
		gbc_txtWeight.anchor = GridBagConstraints.WEST;
		gbc_txtWeight.insets = new Insets(0, 0, 0, 5);
		gbc_txtWeight.gridx = 1;
		gbc_txtWeight.gridy = 1;
		getContentPane().add(txtWeight, gbc_txtWeight);
		txtWeight.setColumns(10);
		
		JLabel lblGrade = new JLabel("Voto");
		lblGrade.setName("lblGrade");
		GridBagConstraints gbc_lblGrade = new GridBagConstraints();
		gbc_lblGrade.insets = new Insets(0, 0, 0, 5);
		gbc_lblGrade.anchor = GridBagConstraints.EAST;
		gbc_lblGrade.gridx = 2;
		gbc_lblGrade.gridy = 1;
		getContentPane().add(lblGrade, gbc_lblGrade);
		
		JComboBox cmbGrade = new JComboBox();
		cmbGrade.setModel(new DefaultComboBoxModel(new String[] {"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "30L"}));
		GridBagConstraints gbc_cmbGrade = new GridBagConstraints();
		gbc_cmbGrade.insets = new Insets(0, 0, 0, 5);
		gbc_cmbGrade.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbGrade.gridx = 3;
		gbc_cmbGrade.gridy = 1;
		getContentPane().add(cmbGrade, gbc_cmbGrade);
		
		JLabel lblDate = new JLabel("Data");
		lblDate.setName("lblDate");
		GridBagConstraints gbc_lblDate = new GridBagConstraints();
		gbc_lblDate.anchor = GridBagConstraints.EAST;
		gbc_lblDate.insets = new Insets(0, 0, 0, 5);
		gbc_lblDate.gridx = 4;
		gbc_lblDate.gridy = 1;
		getContentPane().add(lblDate, gbc_lblDate);
		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		JFormattedTextField txtDate = new JFormattedTextField(format);
		txtDate.setName("txtDate");
		GridBagConstraints gbc_txtDate = new GridBagConstraints();
		gbc_txtDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDate.gridx = 5;
		gbc_txtDate.gridy = 1;
		getContentPane().add(txtDate, gbc_txtDate);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtId;
	private JTextField txtDescription;
	private JTextField txtWeight;

	@Override
	public void showAllExams(List<Exam> exams) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void examAdded(Exam exam) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showError(String message, Exam exam) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void examRemoved(Exam exam) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void examUpdated(Exam exam) {
		// TODO Auto-generated method stub
		
	}

}
