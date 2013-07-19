/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.dma.eclipse.core.jobs.JobManager;
import org.dma.java.utils.Debug;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcp.colibri.workbench.ColibriUI;
import rcp.colibri.workbench.support.IColibriPerspective;
import rcp.magento.workbench.MagentoPerspective;
/*
 * Metodos por ordem de execucao
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}


	public String getInitialWindowPerspectiveId() {
		return MagentoPerspective.ID;
	}


	public void initialize(IWorkbenchConfigurer configurer) {
		ColibriUI.configure(configurer, getInitialWindowPerspectiveId());
	}


	public void postStartup() {
		Debug.out(Activator.PLUGIN_ID);
		if (!ColibriUI.start(new IColibriPerspective[]{new MagentoPerspective()})){
			PlatformUI.getWorkbench().close();
		}
	}


	public final boolean preShutdown() {
		Debug.out(Activator.PLUGIN_ID);
		JobManager.debug();
		return JobManager.getQueuedJobs()==0 && ColibriUI.stop();
	}


	public final void postShutdown() {
		Debug.out(Activator.PLUGIN_ID);
		ColibriUI.dispose();
	}


}
