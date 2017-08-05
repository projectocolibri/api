/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.workbench.actions;

import com.google.code.magja.soap.SoapConfig;

import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.api.IconVARS;
import org.projectocolibri.rcp.colibri.workbench.support.ColibriAction;
import org.projectocolibri.rcp.magento.RCPmagento;
import org.projectocolibri.rcp.magento.dao.MagentoDatabase;

public class MagentoLoginAction extends ColibriAction implements IWorkbenchAction {

	public MagentoLoginAction() {
		setText("Login");
		setImageDescriptor(IconVARS.TOOLBAR_VALIDAR);
	}


	@Override
	public final void run() {

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
			"colibri7", "colibri7", RCPmagento.MAGENTO_API_URL))){
			System.out.println("MAGENTO LOGIN OK!");
			return;
		}

		System.out.println("MAGENTO LOGIN FAILED");

	}

	@Override
	public void dispose() {}


}
