/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcpcolibri.RCPcolibri;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}


	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}


	public void preWindowOpen() {
		getWindowConfigurer().setTitle("Colibri "+RCPcolibri.VERSAO_PROJECTO+" - Example");
		getWindowConfigurer().setInitialSize(new Point(800, 600));
		getWindowConfigurer().setShowPerspectiveBar(true);
	}


	public void postWindowCreate() {
	}


	public void postWindowOpen() {
	}


}
