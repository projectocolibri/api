/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.workbench;

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
import org.projectocolibri.rcp.magento.workbench.actions.MagentoCreateAction;
import org.projectocolibri.rcp.magento.workbench.actions.MagentoListAction;
import org.projectocolibri.rcp.magento.workbench.actions.MagentoLoginAction;

public class Perspective extends WorkbenchHelper implements IColibriPerspective {

	public static final String ID = Perspective.class.getCanonicalName();

	@Override
	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		layout.setFixed(false);

		layout.addView(MagentoView.ID, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
		layout.getViewLayout(MagentoView.ID).setCloseable(false);

		//layout.addPerspectiveShortcut(ID);
		addOtherPerspectiveShortcuts(ID, layout);

	}



	/*
	 * (non-Javadoc)
	 * @see org.projectocolibri.rcp.colibri.workbench.IColibriPerspective
	 */
	@Override
	public Collection<ColibriMenuBarItem> createMenuBar(IWorkbenchWindow window) {

		return Arrays.asList(
		new ColibriMenuBarItem(LABELS.menubar_ficheiro.name(0),
			new ResetPerspectiveAction(),
			new OpenTabelasPreferenceAction(window),
			new ClosePerspectiveAction(ID)),

		new ColibriMenuBarItem("Magento",
			new OpenDebugViewAction(),
			new MagentoLoginAction(),
			new MagentoListAction(),
			new MagentoCreateAction())
		);

	}


	@Override
	public Collection<ColibriCoolBarItem> createCoolBar(IWorkbenchWindow window) {

		return Arrays.asList(
		new ColibriCoolBarItem("Colibri",
			new ResetPerspectiveAction(),
			new OpenTabelasPreferenceAction(window),
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
