/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;

import org.projectocolibri.api.Language.LABELS;
import org.projectocolibri.rcp.colibri.workbench.IColibriPerspective;
import org.projectocolibri.rcp.colibri.workbench.WorkbenchHelper;
import org.projectocolibri.rcp.colibri.workbench.actions.ClosePerspectiveAction;
import org.projectocolibri.rcp.colibri.workbench.actions.OpenDebugViewAction;
import org.projectocolibri.rcp.colibri.workbench.actions.OpenTabelasPreferenceAction;
import org.projectocolibri.rcp.colibri.workbench.actions.ResetPerspectiveAction;
import org.projectocolibri.rcp.colibri.workbench.support.bars.coolbar.ColibriCoolBarItem;
import org.projectocolibri.rcp.colibri.workbench.support.bars.menubar.ColibriMenuBarItem;
import org.projectocolibri.rcp.example.workbench.actions.DatabaseExampleAction;
import org.projectocolibri.rcp.example.workbench.actions.InterfaceExampleAction;
import org.projectocolibri.rcp.example.workbench.actions.ReportExampleAction;

public class ExamplePerspective extends WorkbenchHelper implements IColibriPerspective {

	/* O ID deve ser UNICO */
	public static final String ID = ExamplePerspective.class.getCanonicalName();

	@Override
	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		layout.setFixed(false);

		layout.addView(PerspectiveView.ID, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
		layout.getViewLayout(PerspectiveView.ID).setCloseable(false);

		//layout.addPerspectiveShortcut(ID);
		addOtherPerspectiveShortcuts(ID, layout);

	}



	/*
	 * (non-Javadoc)
	 * @see org.projectocolibri.api.api.rcp.colibri.workbench.IColibriPerspective
	 */
	@Override
	public Collection<ColibriMenuBarItem> createMenuBar(IWorkbenchWindow window) {

		return Arrays.asList(
		new ColibriMenuBarItem(LABELS.menubar_ficheiro.name(0),
			new OpenTabelasPreferenceAction(window),
			new ResetPerspectiveAction(),
			new ClosePerspectiveAction(ID)),

		new ColibriMenuBarItem("Exemplos",
			new InterfaceExampleAction(),
			new DatabaseExampleAction(),
			new ReportExampleAction())
		);

	}


	@Override
	public Collection<ColibriCoolBarItem> createCoolBar(IWorkbenchWindow window) {

		return Arrays.asList(
		new ColibriCoolBarItem("Colibri",
			new OpenTabelasPreferenceAction(window),
			new OpenDebugViewAction(),
			new ResetPerspectiveAction(),
			new ClosePerspectiveAction(ID)),

		new ColibriCoolBarItem("Exemplos",
			new InterfaceExampleAction(),
			new DatabaseExampleAction(),
			new ReportExampleAction())
		);

	}


	@Override
	public boolean saveOnClose() {
		return true;
	}


	@Override
	public String getId() {
		return ID;
	}


}
