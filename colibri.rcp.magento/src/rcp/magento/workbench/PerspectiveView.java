/*******************************************************************************
 * 2008-2012 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.workbench;

import org.dma.eclipse.swt.custom.CustomBrowser;
import org.dma.eclipse.swt.graphics.ImageManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import rcp.colibri.vars.gui.IconVARS;
import rcp.colibri.vars.gui.LabelVARS.LABELS;
import rcp.colibri.workbench.support.views.IViewStateSupport;
import rcp.magento.RCPMagento;

public class PerspectiveView extends ViewPart implements IViewStateSupport {

	public static final String ID = "PerspectiveView";

	public void createPartControl(Composite parent) {

		setPartName(LABELS.desktop_browser.toString());
		setTitleImage(ImageManager.getImage(IconVARS.COOLBAR_BROWSER));

		Composite composite=new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		CustomBrowser browser=new CustomBrowser(composite);
		browser.setUrl(RCPMagento.MAGENTO_ADMIN_URL);

	}


	public void setFocus() {}


	/*
	 * IViewStateSupport(non-Javadoc)
	 * @see rcp.colibri.workbench.support.views.IViewStateSupport#isViewStateValid()
	 */
	public boolean isViewStateValid() {
		return true;
	}


}