/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import java.util.ArrayList;
import java.util.List;

import org.dma.utils.java.Debug;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;

import rcpcolibri.core.ExceptionHandler;
import rcpcolibri.ui.ColibriUI;
import rcpcolibri.ui.workbench.bars.coolbar.ColibriCoolbarItem;
import rcpcolibri.ui.workbench.bars.menubar.ColibriMenubarItem;
import rcpcolibri.ui.workbench.commands.OpenPreferencePageAction;
import rcpcolibri.ui.workbench.commands.ResetPerspectiveAction;
import rcpcolibri.ui.workbench.helpers.WorkbenchHack;
import rcpcolibri.ui.workbench.perspectives.IColibriPerspective;
import rcpcolibri.ui.workbench.views.actions.OpenViewAction;
import rcpcolibri.vars.gui.IconVARS;
import rcpcolibri.vars.gui.LabelVARS;
import rcpcolibri.vars.rcp.ViewVARS;

public class ExamplePerspective implements IColibriPerspective {

	public static final String ID = "ExamplePerspective";

	public void createInitialLayout(IPageLayout layout) {

		try {
			Debug.info(ID);

			layout.setEditorAreaVisible(false);
			layout.setFixed(true);

			//vista
			layout.addView(ExampleView.ID, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
			layout.getViewLayout(ExampleView.ID).setCloseable(false);

			//layout.addPerspectiveShortcut(ID);
			WorkbenchHack.addOtherPerspectiveShortcuts(ID,layout);

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

	}



	//IColibriPerspective
	public List<ColibriCoolbarItem> createCoolbar(IWorkbenchWindow window){

		List<ColibriCoolbarItem> items=new ArrayList();

		items.add(new ColibriCoolbarItem("Colibri", new IAction[]{
			new OpenPreferencePageAction(window)}));

		return items;

	}


	public List<ColibriMenubarItem> createMenubar(IWorkbenchWindow window) {

		List<ColibriMenubarItem> items=new ArrayList();

		OpenViewAction openViewAction=new OpenViewAction(ViewVARS.ArtigosFicheiroView);
		openViewAction.setText(LabelVARS.menu_artigos_ficheiro);
		openViewAction.setImageDescriptor(ColibriUI.getImageDescriptor(IconVARS.COOLBAR_ARTIGOS_FICHEIRO));

		items.add(new ColibriMenubarItem(
			LabelVARS.menubar_ficheiro,
			new IAction[]{
			new OpenPreferencePageAction(window),
			new ResetPerspectiveAction(window),
			openViewAction}));

		return items;

	}


	public boolean saveOnClose() {
		return true;
	}


	public String getId() {
		return ID;
	}


}
