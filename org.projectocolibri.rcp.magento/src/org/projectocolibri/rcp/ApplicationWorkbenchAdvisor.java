/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.dma.java.util.Debug;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import org.projectocolibri.rcp.colibri.RCPcolibri;
import org.projectocolibri.rcp.colibri.RCPcolibri.PREFERENCES;
import org.projectocolibri.rcp.colibri.workbench.ColibriUI;
import org.projectocolibri.rcp.magento.Activator;
import org.projectocolibri.rcp.magento.workbench.Perspective;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		ColibriUI.configure(configurer);
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(PREFERENCES.SAVE_AND_RESTORE.value.getBoolean());
		ColibriUI.configure(getInitialWindowPerspectiveId());
	}

	@Override
	public void postStartup() {
		Debug.out(Activator.PLUGIN_ID);
		if (ColibriUI.start(PREFERENCES.MINIMIZE_TO_TRAY.value.getBoolean(),
				new Perspective())){
			RCPcolibri.run();
		}else{
			PlatformUI.getWorkbench().close();
		}
	}

	@Override
	public final boolean preShutdown() {
		Debug.out(Activator.PLUGIN_ID);
		return ColibriUI.stop(PREFERENCES.CONFIRM_ON_EXIT.value.getBoolean());
	}

	@Override
	public final void postShutdown() {
		Debug.out(Activator.PLUGIN_ID);
		ColibriUI.dispose();
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}

}
