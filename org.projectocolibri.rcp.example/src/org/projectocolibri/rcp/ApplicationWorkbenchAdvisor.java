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
import org.projectocolibri.rcp.example.Activator;
import org.projectocolibri.rcp.example.workbench.ExamplePerspective;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		ColibriUI.configure(configurer);
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	/** Configura o workbench */
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(PREFERENCES.SAVE_AND_RESTORE.value.getBoolean());
		/*
		 * Deve ser passado o ID da perspectiva inicial
		 */
		ColibriUI.configure(getInitialWindowPerspectiveId());
	}

	/** Inicializa o UI */
	@Override
	public void postStartup() {
		Debug.out(Activator.PLUGIN_ID);
	 	/*
	 	 * Devem ser passadas as perspectivas implementadas
	 	 */
		if (ColibriUI.start(PREFERENCES.MINIMIZE_TO_TRAY.value.getBoolean(),
				new ExamplePerspective())){
			/*
			//FACULTATIVO: remove perspectivas
			for (IColibriPerspective perspective: ColibriUI.FIXED_PERSPECTIVES){
				UIHelper.removePerspective(perspective.getId());
			}
			*/
			RCPcolibri.run();
		}else{
			PlatformUI.getWorkbench().close();
		}
	}

	/** Termina o UI */
	@Override
	public final boolean preShutdown() {
		Debug.out(Activator.PLUGIN_ID);
		/*
		 * O metodo que termina o UI controla o fecho do workbench
		 */
		return ColibriUI.stop(PREFERENCES.CONFIRM_ON_EXIT.value.getBoolean());
	}

	@Override
	public final void postShutdown() {
		Debug.out(Activator.PLUGIN_ID);
		ColibriUI.dispose();
	}

	/** Define a perspectiva inicial */
	@Override
	public String getInitialWindowPerspectiveId() {
		/*
		 * A perspectiva inicial e' sempre aberta em primeiro lugar
		 * (desde que nao seja gravada a' saida do Workbench)
		 * e controla a saida da aplicacao atraves do menu
		 * (desde que seja usado o metodo ClosePerspectiveAction)
		 */
		return ExamplePerspective.ID;
	}

}
