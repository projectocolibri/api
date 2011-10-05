/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench.actions;

import org.dma.utils.eclipse.swt.image.ImageUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.colibri.core.vars.gui.IconVARS;
import rcp.colibri.workbench.support.views.actions.OpenViewAction;
import rcp.example.workbench.ExampleView;

public class OpenExampleViewAction extends Action implements IWorkbenchAction {

	public OpenExampleViewAction() {
		setText("#view");
		setImageDescriptor(ImageUtils.getImageDescriptor(IconVARS.COOLBAR_AJUDA));
	}


	public final void run(){

		try{
			new OpenViewAction(ExampleView.ID).run();

		} catch (Exception e){
			e.printStackTrace();
		}

	}

	public void dispose() {}


}
