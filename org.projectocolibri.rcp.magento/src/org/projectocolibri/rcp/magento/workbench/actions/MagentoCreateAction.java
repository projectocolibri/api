/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.workbench.actions;

import org.dma.eclipse.swt.graphics.SWTImageUtils;
import org.dma.java.utils.numeric.NumericUtils;
import org.dma.java.utils.string.StringUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.rcp.colibri.core.vars.IconVARS;
import org.projectocolibri.rcp.magento.dao.MagentoDatabase;

public class MagentoCreateAction extends Action implements IWorkbenchAction {

	public MagentoCreateAction() {
		setText("Create");
		setImageDescriptor(SWTImageUtils.getImageDescriptor(IconVARS.TOOLBAR_GRAVAR));
	}


	public final void run(){

		/*
		 * Pendente
		 * http://code.google.com/p/magja/issues/detail?id=40
		 */
		//MagentoDatabase.getCategoryManager().createCategory(StringUtils.randomLetters(10));
		MagentoDatabase.getProductManager().createProduct("SKU_"+NumericUtils.random(3));
		MagentoDatabase.getCustomerManager().createCustomer(
			StringUtils.randomLetters(10)+"@"+StringUtils.randomLetters(5)+".com");

	}

	public void dispose() {}


}
