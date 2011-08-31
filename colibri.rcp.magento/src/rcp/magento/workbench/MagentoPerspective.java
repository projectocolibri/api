/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.workbench;

import java.util.ArrayList;
import java.util.List;

import org.dma.utils.java.Debug;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;

import rcp.colibri.core.vars.gui.LabelVARS;
import rcp.colibri.workbench.perspectives.IColibriPerspective;
import rcp.colibri.workbench.support.actions.ClosePerspectiveAction;
import rcp.colibri.workbench.support.actions.OpenDebugViewAction;
import rcp.colibri.workbench.support.actions.OpenPreferencePageAction;
import rcp.colibri.workbench.support.actions.ResetPerspectiveAction;
import rcp.colibri.workbench.support.bars.coolbar.ColibriCoolbarItem;
import rcp.colibri.workbench.support.bars.menubar.ColibriMenubarItem;
import rcp.colibri.workbench.support.helpers.WorkbenchHack;
import rcp.magento.workbench.actions.MagentoCreateAction;
import rcp.magento.workbench.actions.MagentoListAction;
import rcp.magento.workbench.actions.MagentoLoginAction;

public class MagentoPerspective implements IColibriPerspective {

	public static final String ID = "MagentoPerspective";

	public void createInitialLayout(IPageLayout layout) {

		try{
			Debug.info(ID);

			layout.setEditorAreaVisible(false);
			//layout.setFixed(true);

			//vista fixa
			layout.addView(PerspectiveView.ID, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
			layout.getViewLayout(PerspectiveView.ID).setCloseable(false);

			//layout.addPerspectiveShortcut(ID);
			WorkbenchHack.addOtherPerspectiveShortcuts(ID,layout);

		} catch (Exception e){
			e.printStackTrace();
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

		items.add(new ColibriMenubarItem(
				LabelVARS.menubar_ficheiro,
				new IAction[]{
				new OpenPreferencePageAction(window),
				new ResetPerspectiveAction(),
				new ClosePerspectiveAction(MagentoPerspective.ID)}));

		items.add(new ColibriMenubarItem("Magento", new IAction[]{
				new OpenDebugViewAction(),
				new MagentoLoginAction(),
				new MagentoListAction(),
				new MagentoCreateAction()}));

		return items;

	}


	public boolean saveOnClose() {
		return true;
	}


	public String getId() {
		return ID;
	}


}
