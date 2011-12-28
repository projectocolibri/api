/*******************************************************************************
 * 2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcp.colibri.workbench.ColibriUI;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		ColibriUI.configure(getWindowConfigurer());
		getWindowConfigurer().setInitialSize(new Point(800, 600));
		getWindowConfigurer().setShowCoolBar(false);
		getWindowConfigurer().setShowStatusLine(true);
		getWindowConfigurer().setShowPerspectiveBar(false);
		getWindowConfigurer().setTitle("RCP Colibri - Importador SAFT");
	}

	public void postWindowCreate() {
	}


	public void postWindowOpen() {
	}


}
