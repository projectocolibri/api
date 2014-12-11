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
import org.projectocolibri.rcp.example.workbench.ExamplePerspective;

/*
 * Metodos por ordem de execucao
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	/**
	 * Configura o workbench
	 * Deve ser passado o ID da perspectiva inicial
	 */
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		ColibriUI.configure(configurer, getInitialWindowPerspectiveId());
	}

	/**
	 * Inicializa o UI.
	 * Devem ser passadas as perspectivas implementadas
	 */
	@Override
	public void postStartup() {
		Debug.out(Activator.PLUGIN_ID);
		if (!ColibriUI.start(new ExamplePerspective())){
			PlatformUI.getWorkbench().close();
		}else{
			RCPcolibri.run();
		}
	}

	/**
	 * Termina o UI.
	 * A aplicacao nao deve ser terminada enquanto existirem processamentos
	 * a correr em background e o User Interface nao conseguir terminar
	 */
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

	/**
	 * A perspectiva inicial e' sempre aberta em primeiro lugar
	 * (desde que nao seja gravada a' saida do Workbench)
	 * e controla a saida da aplicacao atraves do menu
	 * (desde que seja usado o metodo ClosePerspectiveAction)
	 */
	@Override
	public String getInitialWindowPerspectiveId() {
		return ExamplePerspective.ID;
	}

}
