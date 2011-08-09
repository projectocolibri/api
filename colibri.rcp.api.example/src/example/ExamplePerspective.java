/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example;

import org.dma.utils.java.Debug;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rcpcolibri.core.ExceptionHandler;
import rcpcolibri.ui.ColibriUI;
import rcpcolibri.ui.workbench.ColibriCoolbar;
import rcpcolibri.ui.workbench.ColibriMenubar;
import rcpcolibri.ui.workbench.commands.OpenPreferencePageAction;
import rcpcolibri.ui.workbench.helpers.WorkbenchHack;
import rcpcolibri.vars.gui.LabelVARS;

public class ExamplePerspective implements IPerspectiveFactory {

	public static final String ID = "ExamplePerspective";

	public void createInitialLayout(IPageLayout layout) {

		try {
			Debug.info(ID);

			layout.setEditorAreaVisible(false);
			layout.setFixed(true);

			layout.addPerspectiveShortcut(ID);
			WorkbenchHack.addAllPerspectiveShortcuts(layout);

			/*
			 * A perspectiva e' responsavel pela criacao das TOOLBAR e MENUBAR
			 * Uma vez que a contribuicao tem de ser efectuada em TEMPO REAL
			 * na criacao da perspectiva, nao existe a possibilidade de efectuar
			 * o SAVE & RESTORE da mesma
			 */
			buildCoolbar();
			buildMenubar();

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

	}


	public void buildCoolbar() {

		ColibriCoolbar coolbar=ColibriUI.getCoolbar();

		coolbar.add(ID, "Colibri",
			new IAction[]{
			new OpenPreferencePageAction(coolbar.getWindow())});

	}


	public void buildMenubar() {

		ColibriMenubar menubar=ColibriUI.getMenubar();

		menubar.add(ID,	LabelVARS.menubar_ficheiro,
			new IAction[]{new OpenPreferencePageAction(menubar.getWindow())});

	}


}
