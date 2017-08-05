/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.workbench.actions;

import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.api.IconVARS;
import org.projectocolibri.rcp.colibri.workbench.support.ColibriAction;
import org.projectocolibri.rcp.magento.dao.MagentoDatabase;

public class MagentoListAction extends ColibriAction implements IWorkbenchAction {

	public MagentoListAction() {
		setText("List");
		setImageDescriptor(IconVARS.TOOLBAR_ABRIR);
	}


	@Override
	public final void run() {

		/*
		 * Pendente
		 * http://code.google.com/p/magja/issues/detail?id=40
		 */
		//MagentoDatabase.getCategoryManager().listCategories();
		MagentoDatabase.getProductManager().listProducts();
		MagentoDatabase.getCustomerManager().listCustomers();

	}

	@Override
	public void dispose() {}


}
