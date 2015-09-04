/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.workbench.actions;

import org.dma.java.math.NumericUtils;
import org.dma.java.util.StringUtils;

import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.rcp.colibri.core.vars.IconVARS;
import org.projectocolibri.rcp.colibri.workbench.support.ColibriAction;
import org.projectocolibri.rcp.magento.dao.MagentoDatabase;

public class MagentoCreateAction extends ColibriAction implements IWorkbenchAction {

	public MagentoCreateAction() {
		setText("Create");
		setImageDescriptor(IconVARS.TOOLBAR_GRAVAR);
	}


	@Override
	public final void run() {

		/*
		 * Pendente
		 * http://code.google.com/p/magja/issues/detail?id=40
		 */
		//MagentoDatabase.getCategoryManager().createCategory(StringUtils.randomLetters(10));
		MagentoDatabase.getProductManager().createProduct("SKU_"+NumericUtils.random(3));
		MagentoDatabase.getCustomerManager().createCustomer(
			StringUtils.randomLetters(10)+"@"+StringUtils.randomLetters(5)+".com");

	}

	@Override
	public void dispose() {}


}
