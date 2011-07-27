/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcpcolibri.core.language.Language;
import rcpcolibri.ui.ColibriGUI;
import rcpcolibri.ui.workbench.helpers.WorkbenchHelper;
import rcpcolibri.ui.workbench.perspectives.ColibriPerspective;
import example.api.Examples;
import example.api.LoginExample;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return ColibriPerspective.ID;
	}


	public void initialize(IWorkbenchConfigurer configurer) {
		//configura o save & restore
		configurer.setSaveAndRestore(false);
		//configura o workbench
		WorkbenchHelper.configureWorkbench();
		//inicializa locale
		Language.applyLocale();
	}


	public void preStartup() {
		//processou o login?
		if (LoginExample.processLogin()){
		}
	}


	public void postStartup() {
		//verificacao interna - PODE SER REMOVIDA
		//if (RCPcolibri.checkPlugin())

		//inicializa o UI
		if (ColibriGUI.start()){

			new Examples();

		}
	}


	public final boolean preShutdown() {
		//termina o UI
		return ColibriGUI.stop();
	}


}
