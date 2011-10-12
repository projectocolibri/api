/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench;

import org.dma.utils.eclipse.swt.image.ImageManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import rcp.colibri.core.vars.gui.IconVARS;
import rcp.colibri.core.vars.gui.LabelVARS;
import rcp.colibri.core.vars.rcp.FileVARS;
import rcp.colibri.workbench.support.views.IStateSupportView;

public class PerspectiveView extends ViewPart implements IStateSupportView {

	/**
	 * O ID da vista tem de ser UNICO
	 * No pode existir no plugin do Colibri
	 */
	public static final String ID = "PerspectiveView";

	public void createPartControl(Composite parent) {

		setPartName(LabelVARS.desktop_browser);
		setTitleImage(ImageManager.getImage(IconVARS.COOLBAR_BROWSER));

		Composite container=new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		Browser browser=new Browser(container, SWT.NONE);
		browser.setUrl(FileVARS.BROWSER_BASE_URL);

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