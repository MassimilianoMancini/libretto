package com.example.libretto.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.libretto.model.Exam;
import com.example.libretto.model.Grade;

@RunWith(GUITestRunner.class)
public class LibrettoSwingViewTest extends AssertJSwingJUnitTestCase {

	private LibrettoSwingView librettoSwingView;
	private FrameFixture window;

	@Override
	protected void onSetUp() throws Exception {
		GuiActionRunner.execute(()->{
			librettoSwingView = new LibrettoSwingView();
			return librettoSwingView;
		});
		
		window = new FrameFixture(robot(), librettoSwingView);
		window.show();
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
		GuiActionRunner.execute(() -> {
			librettoSwingView.getLstExamModel().addElement(new Exam("B027500", "Data Mining and Organization", 12, new Grade("30L"), LocalDate.of(2020, 1, 29)));
			librettoSwingView.getLstExamModel().addElement(new Exam("B027507", "Parallel Computing", 6, new Grade("27"), LocalDate.of(2020, 1, 9)));
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
	

}
