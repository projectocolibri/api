/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.dma.eclipse.core.jobs.JobManager;
import org.dma.java.utils.Debug;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import org.projectocolibri.rcp.colibri.RCPcolibri;
import org.projectocolibri.rcp.colibri.workbench.ColibriUI;
import org.projectocolibri.rcp.magento.workbench.MagentoPerspective;


/*
 * Metodos por ordem de execucao
 */
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
		Debug.out(Activator.PLUGIN_ID);
		if (!ColibriUI.start(new MagentoPerspective())){
			PlatformUI.getWorkbench().close();
		}else{
			RCPcolibri.run();
		}
	}

	@Override
	public final boolean preShutdown() {
		Debug.out(Activator.PLUGIN_ID);
		JobManager.debug();
		return JobManager.getQueuedJobs()==0 && ColibriUI.stop();
	}

	@Override
	public final void postShutdown() {
		Debug.out(Activator.PLUGIN_ID);
		ColibriUI.dispose();
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return MagentoPerspective.ID;
	}

}
