/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.workbench.actions;

import com.google.code.magja.soap.SoapConfig;

import org.dma.eclipse.swt.graphics.SWTImageUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.rcp.colibri.core.vars.IconVARS;
import org.projectocolibri.rcp.magento.RCPMagento;
import org.projectocolibri.rcp.magento.dao.MagentoDatabase;

public class MagentoLoginAction extends Action implements IWorkbenchAction {

	public MagentoLoginAction() {
		setText("Login");
		setImageDescriptor(SWTImageUtils.getImageDescriptor(IconVARS.TOOLBAR_VALIDAR));
	}


	public final void run(){

		if (MagentoDatabase.getMagentoSoapClient()!=null){
			System.out.println("MAGENTO ALREADY LOGGED");
			return;
		}
		/*
		 * The SOAP parameters must be created in Magento
		 * 1) Create an "admin" ROLE under System -> Web Services -> Roles
		 * and grant access to all the resources
		 * 2) Create an API USER under System -> Web Services -> Users
		 * and assign the created role to the new user
		 */
		if (MagentoDatabase.initialize(new SoapConfig(
			"colibri7","colibri7",RCPMagento.MAGENTO_API_URL))){
			System.out.println("MAGENTO LOGIN OK!");
			return;
		}

		System.out.println("MAGENTO LOGIN FAILED");

	}

	public void dispose() {}


}