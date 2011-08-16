/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcpcolibri.ui.ColibriUI;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}


	public String getInitialWindowPerspectiveId() {
		return MagentoPerspective.ID;
	}


	public void initialize(IWorkbenchConfigurer configurer) {
		//configura o workbench
		rcpcolibri.Application.configure(configurer);
		//regista perspectivas
		ColibriUI.registerPerspective(new MagentoPerspective());
	}


	public void preStartup() {
	}


	public void postStartup() {
		//inicializa o UI
		if (ColibriUI.start()){
		}
	}


	public final boolean preShutdown() {
		//termina o UI
		return ColibriUI.stop();
	}


}
