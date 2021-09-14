package com.example.libretto.view.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.example.libretto.model.Exam;
import com.example.libretto.view.LibrettoView;

public class LibrettoSwingView extends JFrame implements LibrettoView {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtDescription;
	private JFormattedTextField txtWeight;
	private JComboBox<String> cmbGrade;
	private JFormattedTextField txtDate;
	
	private JList<Exam> lstExam;
	private DefaultListModel<Exam> lstExamModel;
	
	
	private JButton btnSave;
	private List<JTextField> fieldList = new ArrayList<>();
	private JScrollPane scrollPane;

	
	public LibrettoSwingView() {
		setTitle("Libretto universitario");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setMinimumSize(new Dimension(7, 20));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		
	gbl_contentPane.columnWidths = new int[] {30, 50, 30, 50, 30, 50, 30, 50, 50};
		gbl_contentPane.rowHeights = new int[]{354, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 9;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		lstExamModel = new DefaultListModel<>();
		lstExam = new JList<>(lstExamModel);
		scrollPane.setViewportView(lstExam);
		lstExam.setName("lstExam");
		
		JLabel lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("lblErrorMessage");
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblErrorMessage.gridwidth = 9;
		gbc_lblErrorMessage.insets = new Insets(0, 0, 5, 5);
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 1;
		getContentPane().add(lblErrorMessage, gbc_lblErrorMessage);
		
		JLabel lblDescription = new JLabel("Descrizione");
		lblDescription.setName("lblDescription");
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.WEST;
		gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescription.gridx = 0;
		gbc_lblDescription.gridy = 2;
		getContentPane().add(lblDescription, gbc_lblDescription);
		
		txtDescription = new JTextField();
		txtDescription.setName("txtDescription");
		GridBagConstraints gbc_txtDescription = new GridBagConstraints();
		gbc_txtDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDescription.gridwidth = 8;
		gbc_txtDescription.insets = new Insets(0, 0, 5, 0);
		gbc_txtDescription.gridx = 1;
		gbc_txtDescription.gridy = 2;
		getContentPane().add(txtDescription, gbc_txtDescription);
		txtDescription.setColumns(10);
		fieldList.add(txtDescription);
		
		JLabel lblId = new JLabel("Codice");
		lblId.setName("lblId");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.insets = new Insets(0, 0, 0, 5);
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 3;
		getContentPane().add(lblId, gbc_lblId);
		
		txtId = new JTextField();
		txtId.setName("txtId");
		GridBagConstraints gbc_txtId = new GridBagConstraints();
		gbc_txtId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtId.insets = new Insets(0, 0, 0, 5);
		gbc_txtId.gridx = 1;
		gbc_txtId.gridy = 3;
		getContentPane().add(txtId, gbc_txtId);
		txtId.setColumns(10);
		
		
		// Add all fields to fieldList
		fieldList.add(txtId);
		
		JLabel lblWeight = new JLabel("Peso");
		lblWeight.setName("lblWeight");
		GridBagConstraints gbc_lblWeight = new GridBagConstraints();
		gbc_lblWeight.anchor = GridBagConstraints.EAST;
		gbc_lblWeight.insets = new Insets(0, 0, 0, 5);
		gbc_lblWeight.gridx = 2;
		gbc_lblWeight.gridy = 3;
		getContentPane().add(lblWeight, gbc_lblWeight);
		
		txtWeight = new JFormattedTextField(NumberFormat.getIntegerInstance());
		txtWeight.setName("txtWeight");
		GridBagConstraints gbc_txtWeight = new GridBagConstraints();
		gbc_txtWeight.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWeight.insets = new Insets(0, 0, 0, 5);
		gbc_txtWeight.gridx = 3;
		gbc_txtWeight.gridy = 3;
		getContentPane().add(txtWeight, gbc_txtWeight);
		txtWeight.setColumns(10);
		fieldList.add(txtWeight);
		
		JLabel lblGrade = new JLabel("Voto");
		lblGrade.setName("lblGrade");
		GridBagConstraints gbc_lblGrade = new GridBagConstraints();
		gbc_lblGrade.anchor = GridBagConstraints.EAST;
		gbc_lblGrade.insets = new Insets(0, 0, 0, 5);
		gbc_lblGrade.gridx = 4;
		gbc_lblGrade.gridy = 3;
		getContentPane().add(lblGrade, gbc_lblGrade);
		
		cmbGrade = new JComboBox<String>();
		cmbGrade.setName("cmbGrade");
		cmbGrade.setModel(new DefaultComboBoxModel<String>(new String[] {"", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "30L"}));
		GridBagConstraints gbc_cmbGrade = new GridBagConstraints();
		gbc_cmbGrade.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbGrade.anchor = GridBagConstraints.NORTH;
		gbc_cmbGrade.insets = new Insets(0, 0, 0, 5);
		gbc_cmbGrade.gridx = 5;
		gbc_cmbGrade.gridy = 3;
		getContentPane().add(cmbGrade, gbc_cmbGrade);
		
		JLabel lblDate = new JLabel("Data");
		lblDate.setName("lblDate");
		GridBagConstraints gbc_lblDate = new GridBagConstraints();
		gbc_lblDate.anchor = GridBagConstraints.EAST;
		gbc_lblDate.insets = new Insets(0, 0, 0, 5);
		gbc_lblDate.gridx = 6;
		gbc_lblDate.gridy = 3;
		getContentPane().add(lblDate, gbc_lblDate);
		
		txtDate = new JFormattedTextField(new SimpleDateFormat("dd-MM-yyyy"));
		txtDate.setName("txtDate");
		GridBagConstraints gbc_txtDate = new GridBagConstraints();
		gbc_txtDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDate.insets = new Insets(0, 0, 0, 5);
		gbc_txtDate.gridx = 7;
		gbc_txtDate.gridy = 3;
		getContentPane().add(txtDate, gbc_txtDate);
		fieldList.add(txtDate);
		
		btnSave = new JButton("Salva");
		btnSave.setEnabled(false);
		btnSave.setName("btnSave");
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSave.anchor = GridBagConstraints.NORTH;
		gbc_btnSave.gridx = 8;
		gbc_btnSave.gridy = 3;
		getContentPane().add(btnSave, gbc_btnSave);
		

		
		// Add a document listener
		DocumentListener listener = new DocumentListener() {
		    @Override
		    public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
		    @Override
		    public void insertUpdate(DocumentEvent e) { changedUpdate(e); }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
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
		};
		
		// Attach documentListener to all text fields and to combo
		for (JTextField tf : fieldList) {
		    tf.getDocument().addDocumentListener(listener);
		}
		
	}
	
	DefaultListModel<Exam> getLstExamModel() {
		return lstExamModel;
	}

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
