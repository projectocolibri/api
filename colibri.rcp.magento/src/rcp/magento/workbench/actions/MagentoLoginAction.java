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
import rcp.magento.dao.MagentoDatabase;

import com.google.code.magja.soap.SoapConfig;

public class MagentoLoginAction extends Action implements IWorkbenchAction {

	public MagentoLoginAction() {
		setText("Login");
		setImageDescriptor(ResourceManager.getImageDescriptor(IconVARS.TOOLBAR_VALIDAR));
	}


	public final void run(){

		Debug.info(getText());

		try{
			if (MagentoDatabase.getMagentoSoapClient()!=null){
				Debug.info("### MAGENTO LOGIN ###", "ALREADY LOGGED");
				return;
			}
			/*
			 * Create a ROLE and USER with full access under
			 * Magento -> System -> Web Services
			 */
			if (MagentoDatabase.initialize(new SoapConfig("colibri7","colibri7",
					RCPMagento.MAGENTO_API_URL))){
				Debug.info("### MAGENTO LOGIN ###", "OK!");
				return;
			}

		} catch (Exception e){
			e.printStackTrace();
		}

		Debug.info("### MAGENTO LOGIN ###", "FAILED");

	}

	public void dispose() {}


}
