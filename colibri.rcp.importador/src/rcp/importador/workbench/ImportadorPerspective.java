/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package rcp.importador.workbench;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;

import rcp.colibri.core.vars.LabelVARS.LABELS;
import rcp.colibri.workbench.support.IColibriPerspective;
import rcp.colibri.workbench.support.WorkbenchHelper;
import rcp.colibri.workbench.support.actions.ClosePerspectiveAction;
import rcp.colibri.workbench.support.actions.OpenTabelasPreferenceAction;
import rcp.colibri.workbench.support.actions.ResetPerspectiveAction;
import rcp.colibri.workbench.support.bars.coolbar.ColibriCoolbarItem;
import rcp.colibri.workbench.support.bars.menubar.ColibriMenubarItem;

public class ImportadorPerspective extends WorkbenchHelper implements IColibriPerspective {

	public static final String ID = "ImportadorPerspective";

	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addStandaloneView(ImportadorView.ID, false, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());

		//layout.addPerspectiveShortcut(ID);
		addOtherPerspectiveShortcuts(ID,layout);

	}



	//IColibriPerspective
	public List<ColibriCoolbarItem> createCoolbar(IWorkbenchWindow window){

		List<ColibriCoolbarItem> items=new ArrayList();

		items.add(new ColibriCoolbarItem("Colibri", new IAction[]{
			new OpenTabelasPreferenceAction(window)}));

		return items;

	}


	public List<ColibriMenubarItem> createMenubar(IWorkbenchWindow window) {

		List<ColibriMenubarItem> items=new ArrayList();

		items.add(new ColibriMenubarItem(LABELS.menubar_ficheiro.toString(), new IAction[]{
				new OpenTabelasPreferenceAction(window),
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
