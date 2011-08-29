/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench;

import org.dma.utils.eclipse.core.resources.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import rcp.colibri.core.handlers.ExceptionHandler;
import rcp.colibri.core.vars.gui.IconVARS;
import rcp.colibri.core.vars.gui.LabelVARS;
import rcp.colibri.core.vars.rcp.FileVARS;

public class PerspectiveView extends ViewPart {

	/**
	 * O ID da vista tem de ser UNICO
	 * No pode existir no plugin do Colibri
	 */
	public static final String ID = "PerspectiveView";

	public void createPartControl(Composite parent) {
		try{
			setPartName(LabelVARS.desktop_browser);
			setTitleImage(SWTResourceManager.getImage(IconVARS.WORKBENCH_BROWSER));

			Composite container=new Composite(parent, SWT.NONE);
			container.setLayout(new FillLayout());

			Browser browser=new Browser(container, SWT.NONE);
			browser.setUrl(FileVARS.BROWSER_BASE_URL);

		} catch (Exception e){
			ExceptionHandler.error(e);
		}
	}


	public void setFocus() {}


}