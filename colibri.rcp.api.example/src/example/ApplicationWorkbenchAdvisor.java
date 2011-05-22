/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@netc.pt)
 *******************************************************************************/
package example;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcpcolibri.RCPcolibri;
import rcpcolibri.core.LoginAction;
import rcpcolibri.dao.model.classes.Entidadesdocumentos;
import rcpcolibri.ui.workbench.helpers.WorkbenchHelper;
import rcpcolibri.ui.workbench.views.actions.OpenViewAction;
import rcpcolibri.vars.rcp.CommandVARS;
import rcpcolibri.vars.rcp.ViewVARS;
import rcpcolibri.vars.security.UserVARS;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return Perspective.PERSPECTIVE_ID;
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
			RCPcolibri.getGUI().start();

			RCPcolibri.getLicenceManager().loadLicence("0");

			LoginAction login=new LoginAction(
				RCPcolibri.getEmpresasXml().getArray()[0],
				UserVARS.USER_ADMIN,
				UserVARS.USER_ADMIN_PASSWORD,
				WorkbenchHelper.getShell());

			if (login.process()){

				new OpenViewAction(ViewVARS.ArtigosFicheiroView).run();

				new OpenViewAction(ViewVARS.EntidadesEmissaoView,
					new Entidadesdocumentos(RCPcolibri.getDBManager().loadDocumentostipos("CFA")),
					CommandVARS.NOVO).run();

				/*
				new OpenPreferencePageAction(WorkbenchHelper.getWorkbenchWindow()).run();
				*/

			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public final boolean preShutdown() {
		try{
			RCPcolibri.getGUI().stop();

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

}
