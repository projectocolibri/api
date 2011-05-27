/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcpcolibri.RCPcolibri;
import rcpcolibri.ui.workbench.ColibriGUI;
import rcpcolibri.ui.workbench.helpers.WorkbenchHelper;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}


	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);

		//configura o save & restore do workbench
		configurer.setSaveAndRestore(false);

		//activa keyboard bindings
		WorkbenchHelper.activateKeyBindings();

	}


	public void preStartup() {
	}


	public void postStartup() {
		try{
			//inicializa o UI
			ColibriGUI.start();

			//verificacao interna - PODE SER REMOVIDA
			if (RCPcolibri.checkExport()){

				new Example();

			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public final boolean preShutdown() {
		try{
			//termina o UI
			ColibriGUI.stop();

		}catch(Exception e){
			e.printStackTrace();
		}

		return true;

	}

}
