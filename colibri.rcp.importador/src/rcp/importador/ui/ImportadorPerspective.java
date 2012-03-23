/*******************************************************************************
 * 2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package rcp.importador.ui;

import java.util.ArrayList;
import java.util.List;

import org.dma.utils.eclipse.ui.WorkbenchHelper;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;

import rcp.colibri.core.vars.gui.LabelVARS;
import rcp.colibri.workbench.perspectives.IColibriPerspective;
import rcp.colibri.workbench.support.actions.ClosePerspectiveAction;
import rcp.colibri.workbench.support.actions.OpenPreferencesAction;
import rcp.colibri.workbench.support.actions.ResetPerspectiveAction;
import rcp.colibri.workbench.support.bars.coolbar.ColibriCoolbarItem;
import rcp.colibri.workbench.support.bars.menubar.ColibriMenubarItem;

public class ImportadorPerspective implements IColibriPerspective {

	public static final String ID = "ImportadorPerspective";

	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		//layout.setFixed(true);

		//vista fixa
		layout.addView(ImportadorView.ID, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
		layout.getViewLayout(ImportadorView.ID).setCloseable(false);

		//layout.addPerspectiveShortcut(ID);
		WorkbenchHelper.addOtherPerspectiveShortcuts(ID,layout);

	}



	//IColibriPerspective
	public List<ColibriCoolbarItem> createCoolbar(IWorkbenchWindow window){

		List<ColibriCoolbarItem> items=new ArrayList();

		items.add(new ColibriCoolbarItem("Colibri", new IAction[]{
			new OpenPreferencesAction(window)}));

		return items;

	}


	public List<ColibriMenubarItem> createMenubar(IWorkbenchWindow window) {

		List<ColibriMenubarItem> items=new ArrayList();

		items.add(new ColibriMenubarItem(LabelVARS.menubar_ficheiro, new IAction[]{
				new OpenPreferencesAction(window),
				new ResetPerspectiveAction(),
				new ClosePerspectiveAction(ID)}));

		return items;

	}


	public boolean saveOnClose() {
		return true;
	}


	public String getId() {
		return ID;
	}


}