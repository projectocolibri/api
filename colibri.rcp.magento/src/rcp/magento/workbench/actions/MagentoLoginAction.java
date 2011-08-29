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

public class MagentoLoginAction extends Action implements IWorkbenchAction {

	public MagentoLoginAction() {
		setText("Login");
		setImageDescriptor(ResourceManager.getImageDescriptor(IconVARS.TOOLBAR_VALIDAR));
	}


	public final void run(){

		Debug.info(getText());

		RCPMagento.login();

	}

	public void dispose() {}


}
