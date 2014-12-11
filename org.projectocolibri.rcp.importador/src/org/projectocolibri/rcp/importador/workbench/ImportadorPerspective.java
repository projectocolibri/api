/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.importador.workbench;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;

import org.projectocolibri.rcp.colibri.core.vars.LabelVARS.LABELS;
import org.projectocolibri.rcp.colibri.workbench.support.IColibriPerspective;
import org.projectocolibri.rcp.colibri.workbench.support.WorkbenchHelper;
import org.projectocolibri.rcp.colibri.workbench.support.actions.ClosePerspectiveAction;
import org.projectocolibri.rcp.colibri.workbench.support.actions.OpenTabelasPreferenceAction;
import org.projectocolibri.rcp.colibri.workbench.support.actions.ResetPerspectiveAction;
import org.projectocolibri.rcp.colibri.workbench.support.bars.coolbar.ColibriCoolBarItem;
import org.projectocolibri.rcp.colibri.workbench.support.bars.menubar.ColibriMenuBarItem;

public class ImportadorPerspective extends WorkbenchHelper implements IColibriPerspective {

	public static final String ID = "ImportadorPerspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		layout.setFixed(true);

		layout.addStandaloneView(ImportadorView.ID, false, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
		layout.getViewLayout(ImportadorView.ID).setCloseable(false);

		//layout.addPerspectiveShortcut(ID);
		addOtherPerspectiveShortcuts(ID,layout);

	}



	/*
	 * IColibriPerspective(non-Javadoc)
	 * @see org.projectocolibri.rcp.colibri.workbench.support.IColibriPerspective#createCoolBar(org.eclipse.ui.IWorkbenchWindow)
	 */
	@Override
	public List<ColibriCoolBarItem> createCoolBar(IWorkbenchWindow window) {

		return Arrays.asList(
		new ColibriCoolBarItem("Colibri",
			new OpenTabelasPreferenceAction(window))
		);

	}


	@Override
	public List<ColibriMenuBarItem> createMenuBar(IWorkbenchWindow window) {

		return Arrays.asList(
		new ColibriMenuBarItem(LABELS.menubar_ficheiro.singular(),
			new OpenTabelasPreferenceAction(window),
			new ResetPerspectiveAction(),
			new ClosePerspectiveAction(ID))
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
