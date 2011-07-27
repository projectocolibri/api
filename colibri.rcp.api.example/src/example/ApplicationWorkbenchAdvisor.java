/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcpcolibri.ui.ColibriGUI;
import rcpcolibri.ui.workbench.helpers.WorkbenchHelper;
import example.api.Examples;
import example.api.LoginExample;

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
		//processou o login?
		if (LoginExample.processLogin()){
		}

	}


	public void postStartup() {
		try{
			//verificacao interna - PODE SER REMOVIDA
			//if (RCPcolibri.checkPlugin())

			//inicializa o UI
			if (ColibriGUI.start()){

				new Examples();

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
