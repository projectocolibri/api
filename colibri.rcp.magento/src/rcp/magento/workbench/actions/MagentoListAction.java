/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.workbench.actions;

import org.dma.utils.eclipse.core.resources.ResourceManager;
import org.dma.utils.java.Debug;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.colibri.core.vars.gui.IconVARS;
import rcp.magento.RCPMagento;

public class MagentoListAction extends Action implements IWorkbenchAction {

	public MagentoListAction() {
		setText("List");
		setImageDescriptor(ResourceManager.getImageDescriptor(IconVARS.TOOLBAR_ABRIR));
	}


	public final void run(){

		Debug.info(getText());

		RCPMagento.listTest();

	}

	public void dispose() {}


}
