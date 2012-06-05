/*******************************************************************************
 * 2008-2012 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.workbench;

import org.dma.utils.eclipse.swt.custom.CustomBrowser;
import org.dma.utils.eclipse.swt.image.ImageManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import rcp.colibri.core.vars.gui.IconVARS;
import rcp.colibri.core.vars.gui.LabelVARS;
import rcp.colibri.workbench.support.views.IStateSupportView;
import rcp.magento.RCPMagento;

public class PerspectiveView extends ViewPart implements IStateSupportView {

	public static final String ID = "PerspectiveView";

	public void createPartControl(Composite parent) {

		setPartName(LabelVARS.desktop_browser.name(0));
		setTitleImage(ImageManager.getImage(IconVARS.COOLBAR_BROWSER));

		Composite composite=new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		CustomBrowser browser=new CustomBrowser(composite);
		browser.setUrl(RCPMagento.MAGENTO_ADMIN_URL);

	}


	public void setFocus() {}


	/*
	 * IStateSupportView(non-Javadoc)
	 * @see rcp.colibri.workbench.support.views.IStateSupportView#isViewStateValid()
	 */
	public boolean isViewStateValid() {
		return true;
	}


}