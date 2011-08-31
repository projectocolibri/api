/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.dma.utils.eclipse.core.jobs.JobManager;
import org.dma.utils.java.Debug;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcp.colibri.RCPcolibri;
import rcp.colibri.workbench.ColibriUI;
import rcp.colibri.workbench.support.helpers.WorkbenchHelper;
import rcp.example.workbench.ExamplePerspective;
/*
 * Metodos por ordem de execucao
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public void initialize(IWorkbenchConfigurer configurer) {
		/*
		 * Regista perspectivas
		 * A primeira perspectiva a ser registada controla a saida da aplicacao
		 * Caso a perspectiva nao seja registada, nao havera' criacao de BARS
		 */
		RCPcolibri.registerPerspective(new ExamplePerspective());
		/*
		 * Configura o workbench
		 * A configuracao do workbench inicializa diversas funcionalidades
		 * Caso o workbench nao seja configurado, nao havera' criacao de BARS
		 */
		RCPcolibri.configure(configurer);
	}


	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}


	public String getInitialWindowPerspectiveId() {
		return ExamplePerspective.ID;
	}


	public void postStartup() {
		/*
		 * Inicializa o UI
		 */
		if (!ColibriUI.start()){
			WorkbenchHelper.getWorkbench().close();
		}
	}


	public final boolean preShutdown() {
		Debug.info(Activator.PLUGIN_ID);
		JobManager.debug();
		/*
		 * Termina o UI
		 * A aplicacao nao deve ser terminada enquanto existirem
		 * processamentos a correr em background
		 */
		return JobManager.getQueuedJobs()==0 && ColibriUI.stop();
	}


}
