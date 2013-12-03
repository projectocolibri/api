/*******************************************************************************
 * 2008-2012 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench;

import org.dma.eclipse.swt.custom.CustomBrowser;
import org.dma.eclipse.swt.graphics.ImageManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import rcp.colibri.core.vars.IconVARS;
import rcp.colibri.core.vars.LabelVARS.LABELS;
import rcp.colibri.workbench.support.views.IViewStateSupport;

public class PerspectiveView extends ViewPart implements IViewStateSupport {

	/**
	 * O ID da vista tem de ser UNICO
	 * No pode existir no plugin do Colibri
	 */
	public static final String ID = "PerspectiveView";

	public void createPartControl(Composite parent) {

		setPartName(LABELS.desktop_browser.toString());
		setTitleImage(ImageManager.getImage(IconVARS.COOLBAR_BROWSER));

		Composite composite=new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		CustomBrowser browser=new CustomBrowser(composite);
		browser.setUrl("http://www.projectocolibri.com/");

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