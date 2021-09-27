package com.example.libretto.view.swing;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.DefaultListModel;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.libretto.controller.LibrettoController;
import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;

@RunWith(GUITestRunner.class)
public class LibrettoSwingViewTest extends AssertJSwingJUnitTestCase {

	private LibrettoSwingView librettoSwingView;
	private FrameFixture window;
	private static final String DATE_FORMAT_IT = "dd-MM-yyyy";


	@Mock
	private LibrettoController librettoController;

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			librettoSwingView = new LibrettoSwingView();
			librettoSwingView.setLibrettoController(librettoController);
			return librettoSwingView;
		});
		
		robot().settings().delayBetweenEvents(200);
		robot().settings().eventPostingDelay(200);
		

		window = new FrameFixture(robot(), librettoSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testControlsInitialStates() {
		window.label("lblListHeader").requireVisible();
		assertThat(window.label("lblId")).isNotNull();
		window.textBox("txtId").requireEnabled();
		assertThat(window.label("lblDescription")).isNotNull();
		window.textBox("txtDescription").requireEnabled();
		assertThat(window.label("lblWeight")).isNotNull();
		window.textBox("txtWeight").requireEnabled();
		assertThat(window.label("lblGrade")).isNotNull();
		window.comboBox("cmbGrade").requireEnabled();
		assertThat(window.label("lblDate")).isNotNull();
		window.textBox("txtDate").requireEnabled();
		window.button("btnSave").requireDisabled();
		assertThat(window.list("lstExam")).isNotNull();
		window.label("lblErrorMessage").requireText(" ");
		window.label("lblAverage").requireText("Media");
		window.label("lblWeightedAverage").requireText("Media pesata");
		window.textBox("txtAverage").requireText(" ");
		window.textBox("txtWeightedAverage").requireText(" ");
		window.textBox("txtAverage").requireNotEditable();
		window.textBox("txtWeightedAverage").requireNotEditable();
		window.button("btnEdit").requireDisabled();
		window.button("btnDelete").requireDisabled();
		GuiActionRunner.execute(() -> {
			librettoSwingView.getLstExamModel().addElement(new Exam("B027500", "Data Mining and Organization", 12,
					new Grade("30L"), LocalDate.of(2020, 1, 29)));
			librettoSwingView.getLstExamModel().addElement(
					new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
		});

		window.list("lstExam").selectItem(0);
		window.list("lstExam").selectItem(1);
	}

	@Test
	public void testWhenAllFieldsAreFilledSaveButtonIsEnabled() {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").setText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2020");
		assertThat(window.button("btnSave").isEnabled()).isTrue();
	}

	@Test
	public void testWhenIdFieldIsEmptySaveButtonIsDisabled() {
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").setText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2020");
		assertThat(window.button("btnSave").isEnabled()).isFalse();
	}

	@Test
	public void testWhenDescriptionFieldIsEmptySaveButtonIsDisabled() {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtWeight").setText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2020");
		assertThat(window.button("btnSave").isEnabled()).isFalse();
	}

	@Test
	public void testWhenWeightFieldIsEmptySaveButtonIsDisabled() {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2020");
		assertThat(window.button("btnSave").isEnabled()).isFalse();
	}

	@Test
	public void testWhenGradeComboIsNotSelectedSaveButtonIsDisabled() {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").setText("6");
		window.textBox("txtDate").setText("09-01-2020");
		window.comboBox("cmbGrade").clearSelection();
		assertThat(window.button("btnSave").isEnabled()).isFalse();
	}

	@Test
	public void testWhenDateFieldIsEmptySaveButtonIsDisabled() {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").setText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("");
		assertThat(window.button("btnSave").isEnabled()).isFalse();
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAnExamIsSelected() {
		GuiActionRunner.execute(() -> {
			librettoSwingView.getLstExamModel().addElement(new Exam("B027500", "Data Mining and Organization", 12,
					new Grade("30L"), LocalDate.of(2020, 1, 29)));
			librettoSwingView.getLstExamModel().addElement(
					new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
		});
		window.list("lstExam").selectItem(1);
		JButtonFixture btnDelete = window.button("btnDelete");
		assertThat(btnDelete.isEnabled()).isTrue();
		window.list("lstExam").clearSelection();
		assertThat(btnDelete.isEnabled()).isFalse();
	}
	
	@Test
	public void testEditButtonShouldBeEnabledOnlyWhenAnExamIsSelected() {
		GuiActionRunner.execute(() -> {
			librettoSwingView.getLstExamModel().addElement(new Exam("B027500", "Data Mining and Organization", 12,
					new Grade("30L"), LocalDate.of(2020, 1, 29)));
			librettoSwingView.getLstExamModel().addElement(
					new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
		});
		window.list("lstExam").selectItem(1);
		JButtonFixture btnEdit = window.button("btnEdit");
		assertThat(btnEdit.isEnabled()).isTrue();
		window.list("lstExam").clearSelection();
		assertThat(btnEdit.isEnabled()).isFalse();
	}
	
	@Test
	public void testClickOnEditButtonLoadsFieldsValue() {
		GuiActionRunner.execute(() -> {
			librettoSwingView.getLstExamModel().addElement(new Exam("B027500", "Data Mining and Organization", 12,
					new Grade("30L"), LocalDate.of(2020, 1, 29)));
			librettoSwingView.getLstExamModel().addElement(
					new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
		});
		
		window.list("lstExam").selectItem(1);
		window.button("btnEdit").click();
		window.textBox("txtId").requireText("B027507");
		window.textBox("txtDescription").requireText("Parallel Computing");
		window.textBox("txtWeight").requireText("6");
		window.comboBox("cmbGrade").requireSelection("27");
		window.textBox("txtDate").requireText("09-01-2020");
		
		// and txtId is not editable
		window.textBox("txtId").requireNotEditable();
		
		// and save button is enabled
		window.button("btnSave").requireEnabled();
	}
	
	@Test
	public void testShowAllExamsShouldAddExamDescriptionToTheList() {
		Exam exam1 = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		Exam exam2 = new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		GuiActionRunner.execute(() -> librettoSwingView.showAllExams(asList(exam1, exam2)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(getDisplayListString(exam1), getDisplayListString(exam2));
		window.textBox("txtAverage").requireText("28.50");
		window.textBox("txtWeightedAverage").requireText("29.00");
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		GuiActionRunner.execute(() -> librettoSwingView.showError("Errore"));
		window.label("lblErrorMessage").requireText("Errore");
	}

	@Test
	public void testAddAnExamUpdateTheListTheAveragesAndResetErrorMessage() {
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		GuiActionRunner.execute(() -> librettoSwingView.examAdded(exam));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(getDisplayListString(exam));

		window.textBox("txtAverage").requireText("30.00");
		window.textBox("txtWeightedAverage").requireText("30.00");
		window.label("lblErrorMessage").requireText(" ");
	}

	@Test
	public void testRemoveAnExamUpdateTheListTheAveragesAndResetErrorMessage() {
		Exam exam1 = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		Exam exam2 = new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		GuiActionRunner.execute(() -> {
			DefaultListModel<Exam> lstExamModel = librettoSwingView.getLstExamModel();
			lstExamModel.addElement(exam1);
			lstExamModel.addElement(exam2);
		});

		GuiActionRunner.execute(() -> librettoSwingView.examRemoved(exam1));

		assertThat(window.list().contents()).containsExactly(getDisplayListString(exam2));

		window.textBox("txtAverage").requireText("27.00");
		window.textBox("txtWeightedAverage").requireText("27.00");
		window.label("lblErrorMessage").requireText(" ");
	}

	@Test
	public void testSaveButtonShouldDelegateToLibrettoControllerNewExam() throws IllegalArgumentException, SQLException {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").enterText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2020");
		window.button("btnSave").click();
		verify(librettoController).newExam(new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
	}
	
	@Test
	public void testSaveButtonShouldDelegateToLibrettoControllerUpdateExam() throws IllegalArgumentException, SQLException {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").enterText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2020");
		
		GuiActionRunner.execute(() -> librettoSwingView.getTxtID().setEditable(false));
		
		window.button("btnSave").click();
		verify(librettoController).updateExam(new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
	}
	
	@Test
	public void testDeleteButtonShouldDelegateToLibrettoControllerDeleteExam() throws SQLException {
		Exam exam1 = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		Exam exam2 = new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		GuiActionRunner.execute(() -> {
			DefaultListModel<Exam> lstExamModel = librettoSwingView.getLstExamModel();
			lstExamModel.addElement(exam1);
			lstExamModel.addElement(exam2);
		});
		window.list("lstExam").selectItem(1);
		window.button("btnDelete").click();
		window.optionPane().yesButton().click();
		verify(librettoController).deleteExam(exam2);
	}

	@Test
	public void testShowErrorExamNotFound() {
		Exam exam1 = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		Exam exam2 = new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		GuiActionRunner.execute(() -> {
			DefaultListModel<Exam> lstExamModel = librettoSwingView.getLstExamModel();
			lstExamModel.addElement(exam1);
			lstExamModel.addElement(exam2);
		});
		GuiActionRunner.execute(() -> librettoSwingView.showErrorExamNotFound("Messaggio di errore", exam1));
		window.label("lblErrorMessage").requireText("Messaggio di errore: " + getDisplayErrorString(exam1));

		assertThat(window.list().contents()).containsExactly(getDisplayListString(exam2));
	}

	@Test
	public void testShowErrorExamAlreadyExistsAndAddExam() {
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		GuiActionRunner.execute(() -> librettoSwingView.showErrorExamAlreadyExists("Messaggio di errore", exam));
		window.label("lblErrorMessage").requireText("Messaggio di errore: " + getDisplayErrorString(exam));

		assertThat(window.list().contents()).containsExactly(getDisplayListString(exam));
	}

	@Test
	public void testShowErrorExamAlreadyExistsAndAddExamIfNotPresentYet() {
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		GuiActionRunner.execute(() -> {
			DefaultListModel<Exam> lstExamModel = librettoSwingView.getLstExamModel();
			lstExamModel.addElement(exam);
		});
		GuiActionRunner.execute(() -> librettoSwingView.showErrorExamAlreadyExists("Messaggio di errore", exam));
		window.label("lblErrorMessage").requireText("Messaggio di errore: " + getDisplayErrorString(exam));

		assertThat(window.list().contents()).containsOnlyOnce(getDisplayListString(exam));
	}

	@Test
	public void testDateInIncorrectFormShowErrorMessage() {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").enterText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09/01/2020");
		window.button("btnSave").click();
		window.label("lblErrorMessage").requireText("Il formato data è gg-mm-aaaa");
	}
	
	@Test
	public void testFutureDateShowErrorMessage() {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").enterText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2030");
		window.button("btnSave").click();
		window.label("lblErrorMessage").requireText("Non sono ammesse date future");
	}

	private String getDisplayListString(Exam exam) {
		return String.format("%-7s|%-60s|%4d|%4s|%10s", exam.getId(), exam.getDescription(), exam.getWeight(),
				exam.getGrade().getValue(), exam.getDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT_IT)));
	}

	private String getDisplayErrorString(Exam exam) {
		return String.format("%-7s - %20s (%2d) %3s %10s", exam.getId(), exam.getDescription().trim(), exam.getWeight(),
				exam.getGrade().getValue(), exam.getDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT_IT)));
	}

}
