/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.workbench;

import org.dma.utils.eclipse.core.resources.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import rcp.colibri.core.vars.gui.IconVARS;
import rcp.colibri.core.vars.gui.LabelVARS;
import rcp.magento.RCPMagento;

public class PerspectiveView extends ViewPart {

	public static final String ID = "PerspectiveView";

	public void createPartControl(Composite parent) {
		try{
			setPartName(LabelVARS.desktop_browser);
			setTitleImage(SWTResourceManager.getImage(IconVARS.WORKBENCH_BROWSER));

			Composite container=new Composite(parent, SWT.NONE);
			container.setLayout(new FillLayout());

			Browser browser=new Browser(container, SWT.NONE);
			browser.setUrl(RCPMagento.MAGENTO_ADMIN_URL);

		} catch (Exception e){
			e.printStackTrace();
		}
	}


	public void setFocus() {}


}