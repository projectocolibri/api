/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcpcolibri.core.ExceptionHandler;
import rcpcolibri.core.LoginAction;
import rcpcolibri.core.security.LicenceManager;
import rcpcolibri.dao.database.DatabaseManager;
import rcpcolibri.dao.model.classes.Entidadesdocumentos;
import rcpcolibri.ui.workbench.ColibriGUI;
import rcpcolibri.ui.workbench.commands.OpenPreferencePageAction;
import rcpcolibri.ui.workbench.helpers.WorkbenchHelper;
import rcpcolibri.ui.workbench.views.actions.OpenViewAction;
import rcpcolibri.vars.database.DatanucleusVARS;
import rcpcolibri.vars.rcp.CommandVARS;
import rcpcolibri.vars.rcp.FileVARS;
import rcpcolibri.vars.rcp.ViewVARS;
import rcpcolibri.vars.security.UserVARS;
import rcpcolibri.xml.empresas.DbcolibriDocument.Dbcolibri;
import rcpcolibri.xml.empresas.EmpresaDocument.Empresa;

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
			ColibriGUI.start();

			LicenceManager.loadLicence(
				FileVARS.LICENCE_FOLDER+"INTERNACIONAL.rcplicence", "0");

			LoginAction login=new LoginAction(
				createEmpresa(),
				UserVARS.USER_ADMIN,
				UserVARS.USER_ADMIN_PASSWORD,
				WorkbenchHelper.getShell());

			if (login.process()){

				/*
				new OpenViewAction(ViewVARS.ArtigosFicheiroView).run();
				*/

				new OpenViewAction(ViewVARS.EntidadesEmissaoView,
					new Entidadesdocumentos(DatabaseManager.loadDocumentostipos("CFA")),
					CommandVARS.NOVO).run();

				new OpenPreferencePageAction(WorkbenchHelper.getWorkbenchWindow()).run();

			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public Empresa createEmpresa(){
		try {
			Empresa empresa = Empresa.Factory.newInstance();
			empresa.setNome("Colibri");

			Dbcolibri dbcolibri = Dbcolibri.Factory.newInstance();
			dbcolibri.setDriver(DatanucleusVARS.DRIVER_H2);
			dbcolibri.setHost("");
			dbcolibri.setDatabase("/colibri/rcp6");
			dbcolibri.setUtilizador("sa");
			dbcolibri.setPassword("");
			empresa.setDbcolibri(dbcolibri);

			return empresa;

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

		return null;

	}


	public final boolean preShutdown() {
		try{
			ColibriGUI.stop();

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

}
