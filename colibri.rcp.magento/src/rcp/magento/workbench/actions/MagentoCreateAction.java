/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.workbench.actions;

import org.dma.utils.eclipse.swt.image.ImageUtils;
import org.dma.utils.java.numeric.IntegerUtils;
import org.dma.utils.java.string.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.colibri.core.vars.gui.IconVARS;
import rcp.magento.dao.MagentoDatabase;

public class MagentoCreateAction extends Action implements IWorkbenchAction {

	public MagentoCreateAction() {
		setText("Create");
		setImageDescriptor(ImageUtils.getImageDescriptor(IconVARS.TOOLBAR_GRAVAR));
	}


	public final void run(){

		try{
			/*
			 * Pendente
			 * http://code.google.com/p/magja/issues/detail?id=40
			 */
			//MagentoDatabase.getCategoryManager().createCategory(StringUtils.random(10));
			MagentoDatabase.getProductManager().createProduct("SKU_"+IntegerUtils.random(3));
			MagentoDatabase.getCustomerManager().createCustomer(
				StringUtils.random(10)+"@"+StringUtils.random(5)+".com");

		} catch (Exception e){
			e.printStackTrace();
		}

	}

	public void dispose() {}


}
