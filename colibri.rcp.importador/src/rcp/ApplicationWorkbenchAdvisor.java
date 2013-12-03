/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcp.colibri.workbench.ColibriUI;
import rcp.importador.RCPImportador;
import rcp.importador.workbench.ImportadorPerspective;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public void initialize(IWorkbenchConfigurer configurer) {
		ColibriUI.configure(configurer, getInitialWindowPerspectiveId());
	}

	public void preStartup() {
	}

	public void postStartup() {
		//nao inicializa UI
		new RCPImportador();
	}

	public final boolean preShutdown() {
		return true;
	}

	public String getInitialWindowPerspectiveId() {
		return ImportadorPerspective.ID;
	}

}
