/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench.actions;

import org.dma.utils.eclipse.core.resources.ResourceManager;
import org.dma.utils.java.Debug;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.colibri.core.vars.gui.IconVARS;
import rcp.colibri.workbench.support.views.actions.OpenViewAction;
import rcp.example.workbench.ExampleView;

public class OpenExampleViewAction extends Action implements IWorkbenchAction {

	public OpenExampleViewAction() {
		setText("#view");
		setImageDescriptor(ResourceManager.getImageDescriptor(IconVARS.COOLBAR_AJUDA));
	}


	public final void run(){

		Debug.info(getText());

		try{
			new OpenViewAction(ExampleView.ID).run();

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void dispose() {}


}
