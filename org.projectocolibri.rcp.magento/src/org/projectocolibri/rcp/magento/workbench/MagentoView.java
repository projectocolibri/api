/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.workbench;

import org.dma.eclipse.swt.custom.CustomBrowser;
import org.dma.eclipse.swt.graphics.ImageManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.projectocolibri.api.IconVARS;
import org.projectocolibri.api.Language.LABELS;
import org.projectocolibri.rcp.colibri.workbench.support.views.IViewStateSupport;
import org.projectocolibri.rcp.magento.RCPmagento;

public class MagentoView extends ViewPart implements IViewStateSupport {

	public static final String ID = MagentoView.class.getCanonicalName();

	@Override
	public void createPartControl(Composite parent) {

		setPartName(LABELS.desktop_browser.name(0));
		setTitleImage(ImageManager.getImage(IconVARS.COOLBAR_BROWSER));

		Composite composite=new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		CustomBrowser browser=new CustomBrowser(composite);
		browser.setUrl(RCPmagento.MAGENTO_ADMIN_URL);

	}


	@Override
	public void setFocus() {}



	/*
	 * (non-Javadoc)
	 * @see org.projectocolibri.rcp.colibri.workbench.support.views.IViewStateSupport
	 */
	@Override
	public boolean isRestorable() {
		return true;
	}


}