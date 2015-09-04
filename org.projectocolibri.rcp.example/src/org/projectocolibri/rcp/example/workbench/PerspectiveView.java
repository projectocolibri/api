/*******************************************************************************
 * 2008-2012 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench;

import org.dma.eclipse.swt.custom.CustomBrowser;
import org.dma.eclipse.swt.graphics.ImageManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.projectocolibri.rcp.colibri.core.language.Language.LABELS;
import org.projectocolibri.rcp.colibri.core.vars.IconVARS;
import org.projectocolibri.rcp.colibri.workbench.support.views.IViewStateSupport;

public class PerspectiveView extends ViewPart implements IViewStateSupport {

	/**
	 * O ID da vista tem de ser UNICO
	 * No pode existir no plugin do Colibri
	 */
	public static final String ID = "PerspectiveView";

	@Override
	public void createPartControl(Composite parent) {

		setPartName(LABELS.desktop_browser.singular());
		setTitleImage(ImageManager.createImage(IconVARS.COOLBAR_BROWSER));

		Composite composite=new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		CustomBrowser browser=new CustomBrowser(composite);
		browser.setUrl("http://www.projectocolibri.com/");

	}


	@Override
	public void setFocus() {}



	/*
	 * (non-Javadoc)
	 * @see org.projectocolibri.rcp.colibri.workbench.support.views.IViewStateSupport#isRestorable()
	 */
	@Override
	public boolean isRestorable() {
		return true;
	}


}