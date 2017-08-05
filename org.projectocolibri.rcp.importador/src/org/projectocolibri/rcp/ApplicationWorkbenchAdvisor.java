/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.dma.java.util.Debug;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import org.projectocolibri.api.database.ColibriDatabase;
import org.projectocolibri.rcp.importador.Activator;
import org.projectocolibri.rcp.importador.RCPimportador;
import org.projectocolibri.rcp.importador.workbench.Perspective;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		configurer.setInitialSize(new Point(16*50, 9*50));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(true);
		configurer.setShowPerspectiveBar(false);
		configurer.setTitle("Projecto Colibri | Importador SAFT");
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(true);
	}

	@Override
	public void postStartup() {
		Debug.out(Activator.PLUGIN_ID);
		new RCPimportador();
	}

	@Override
	public final void postShutdown() {
		Debug.out(Activator.PLUGIN_ID);
		ColibriDatabase.close();
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}

}
