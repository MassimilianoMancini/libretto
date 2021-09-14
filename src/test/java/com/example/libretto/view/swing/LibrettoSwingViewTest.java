package com.example.libretto.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

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
		window.textBox("txtdweight").requireEnabled();
		assertThat(window.label("lblGrade")).isNotNull();
		window.textBox("txtGrade").requireEnabled();
		assertThat(window.label("lblDate")).isNotNull();
		window.textBox("txtDate").requireEnabled();
		window.button("btnAdd").requireDisabled();
		assertThat(window.list("lstExam")).isNotNull();
		window.button("btnDelete").requireDisabled();
		window.label("lblErrorMessage").requireText(" ");
	}
	

}
