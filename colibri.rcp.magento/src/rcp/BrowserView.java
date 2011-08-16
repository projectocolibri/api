/*******************************************************************************
 * 2008-2010 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import rcpcolibri.core.ExceptionHandler;
import rcpcolibri.ui.ColibriUI;
import rcpcolibri.ui.workbench.views.IStateSupportView;
import rcpcolibri.vars.gui.IconVARS;

public class BrowserView extends ViewPart implements IStateSupportView {

	public static final String ID = "BrowserView";

	private Composite container;
	private Browser browser;

	public void createPartControl(Composite parent) {
		try {
			setPartName("Browser");
			setTitleImage(ColibriUI.getImage(IconVARS.COOLBAR_AJUDA));

			container=new Composite(parent, SWT.NONE);
			container.setLayout(new FillLayout());

			browser=new Browser(container, SWT.NONE);
			browser.setUrl("http://marcolopes.pt.to");

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}
	}


	public void setFocus() {}






	//IStateSupportView
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
	}

	public boolean isViewStateValid() {
		return true;
	}


}