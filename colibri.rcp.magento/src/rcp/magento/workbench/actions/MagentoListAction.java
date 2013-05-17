/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.workbench.actions;

import org.dma.eclipse.swt.graphics.SWTImageUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.colibri.vars.gui.IconVARS;
import rcp.magento.dao.MagentoDatabase;

public class MagentoListAction extends Action implements IWorkbenchAction {

	public MagentoListAction() {
		setText("List");
		setImageDescriptor(SWTImageUtils.getImageDescriptor(IconVARS.TOOLBAR_ABRIR));
	}


	public final void run(){

		/*
		 * Pendente
		 * http://code.google.com/p/magja/issues/detail?id=40
		 */
		//MagentoDatabase.getCategoryManager().listCategories();
		MagentoDatabase.getProductManager().listProducts();
		MagentoDatabase.getCustomerManager().listCustomers();

	}

	public void dispose() {}


}
