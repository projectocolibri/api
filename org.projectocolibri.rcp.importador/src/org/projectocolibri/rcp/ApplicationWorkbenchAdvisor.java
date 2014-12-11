/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import org.projectocolibri.rcp.colibri.workbench.ColibriUI;
import org.projectocolibri.rcp.importador.RCPImportador;
import org.projectocolibri.rcp.importador.workbench.ImportadorPerspective;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		ColibriUI.configure(configurer, getInitialWindowPerspectiveId());
	}

	@Override
	public void postStartup() {
		//nao inicializa UI
		new RCPImportador();
	}

	@Override
	public final boolean preShutdown() {
		return true;
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return ImportadorPerspective.ID;
	}

}
