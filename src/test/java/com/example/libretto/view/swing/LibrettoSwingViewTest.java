package com.example.libretto.view.swing;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.time.LocalDate;

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

		window = new FrameFixture(robot(), librettoSwingView);
		window.show();
	}
	
	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testControlsInitialStates() {
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
		window.textBox("txtAverage").requireText("0.0");
		window.textBox("txtWeightedAverage").requireText("0.0");
		window.textBox("txtAverage").requireNotEditable();
		window.textBox("txtWeightedAverage").requireNotEditable();
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
		assertThat(window.button("btnSave").isEnabled()).isFalse();
	}

	@Test
	public void testWhenDateFieldIsEmptySaveButtonIsDisabled() {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").setText("6");
		window.comboBox("cmbGrade").selectItem(10);
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
	public void testShowAllExamsShouldAddExamDescriptionToTheList() {
		Exam exam1 = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		Exam exam2 = new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		GuiActionRunner.execute(() -> librettoSwingView.showAllExams(asList(exam1, exam2)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(exam1.toString(), exam2.toString());
		window.textBox("txtAverage").requireText("28.5");
		window.textBox("txtWeightedAverage").requireText("29.0");
	}
	
	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		GuiActionRunner.execute(() -> librettoSwingView.showError("Errore", exam));
		window.label("lblErrorMessage").requireText("Errore: " + exam);
	}
	
	@Test
	public void testAddAnExamUpdateTheListTheAveragesAndResetErrorMessage() {
		Exam exam = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"),
				LocalDate.of(2020, 1, 29));
		GuiActionRunner.execute(() -> librettoSwingView.examAdded(exam));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(exam.toString());
		window.textBox("txtAverage").requireText("30.0");
		window.textBox("txtWeightedAverage").requireText("30.0");
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
		
		assertThat(window.list().contents()).containsExactly(exam2.toString());
		window.textBox("txtAverage").requireText("27.0");
		window.textBox("txtWeightedAverage").requireText("27.0");
		window.label("lblErrorMessage").requireText(" ");
	}
	
	@Test
	public void testAddButtonShouldDelegateToLibrettoControllerNewExam() throws IllegalArgumentException, SQLException {
		window.textBox("txtId").setText("B027507");
		window.textBox("txtDescription").setText("Parallel Computing");
		window.textBox("txtWeight").enterText("6");
		window.comboBox("cmbGrade").selectItem(10);
		window.textBox("txtDate").setText("09-01-2020");
		window.button("btnSave").click();
		verify(librettoController).newExam(new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
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
		verify(librettoController).deleteExam(exam2);
	}
	
	@Test
	public void testShowErrorExamNotFound() {
		Exam exam1 = new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"), LocalDate.of(2020, 1, 29));
		Exam exam2 = new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9));
		GuiActionRunner.execute(() -> {
			DefaultListModel<Exam> lstExamModel = librettoSwingView.getLstExamModel();
			lstExamModel.addElement(exam1);
			lstExamModel.addElement(exam2);
		});
		GuiActionRunner.execute(() -> librettoSwingView.showErrorExamNotFound("Messaggio di errore", exam1));
		window.label("lblErrorMessage").requireText("Messaggio di errore: " + exam1);
		assertThat(window.list().contents()).containsExactly(exam2.toString());
	}
}
