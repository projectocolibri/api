/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.workbench;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;

import org.projectocolibri.rcp.colibri.core.vars.LabelVARS.LABELS;
import org.projectocolibri.rcp.colibri.workbench.support.IColibriPerspective;
import org.projectocolibri.rcp.colibri.workbench.support.WorkbenchHelper;
import org.projectocolibri.rcp.colibri.workbench.support.actions.ClosePerspectiveAction;
import org.projectocolibri.rcp.colibri.workbench.support.actions.OpenDebugViewAction;
import org.projectocolibri.rcp.colibri.workbench.support.actions.OpenTabelasPreferenceAction;
import org.projectocolibri.rcp.colibri.workbench.support.actions.ResetPerspectiveAction;
import org.projectocolibri.rcp.colibri.workbench.support.bars.coolbar.ColibriCoolBarItem;
import org.projectocolibri.rcp.colibri.workbench.support.bars.menubar.ColibriMenuBarItem;
import org.projectocolibri.rcp.magento.workbench.actions.MagentoCreateAction;
import org.projectocolibri.rcp.magento.workbench.actions.MagentoListAction;
import org.projectocolibri.rcp.magento.workbench.actions.MagentoLoginAction;

public class MagentoPerspective extends WorkbenchHelper implements IColibriPerspective {

	public static final String ID = "MagentoPerspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		layout.setFixed(false);

		layout.addView(PerspectiveView.ID, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
		layout.getViewLayout(PerspectiveView.ID).setCloseable(false);

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
			new ResetPerspectiveAction(),
			new OpenTabelasPreferenceAction(window),
			new ClosePerspectiveAction(MagentoPerspective.ID))
		);

	}


	@Override
	public List<ColibriMenuBarItem> createMenuBar(IWorkbenchWindow window) {

		return Arrays.asList(
		new ColibriMenuBarItem(LABELS.menubar_ficheiro.singular(),
			new ResetPerspectiveAction(),
			new OpenTabelasPreferenceAction(window),
			new ClosePerspectiveAction(MagentoPerspective.ID)),

		new ColibriMenuBarItem("Magento",
			new OpenDebugViewAction(),
			new MagentoLoginAction(),
			new MagentoListAction(),
			new MagentoCreateAction())
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
