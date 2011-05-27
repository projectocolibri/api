/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example;

import rcpcolibri.core.ExceptionHandler;
import rcpcolibri.core.LoginAction;
import rcpcolibri.core.security.LicenceManager;
import rcpcolibri.dao.database.DatabaseManager;
import rcpcolibri.dao.model.classes.Entidadesdocumentos;
import rcpcolibri.ui.workbench.helpers.WorkbenchHelper;
import rcpcolibri.ui.workbench.views.actions.OpenViewAction;
import rcpcolibri.vars.database.DatanucleusVARS;
import rcpcolibri.vars.rcp.CommandVARS;
import rcpcolibri.vars.rcp.FileVARS;
import rcpcolibri.vars.rcp.ViewVARS;
import rcpcolibri.vars.security.UserVARS;
import rcpcolibri.xml.empresas.DbcolibriDocument.Dbcolibri;
import rcpcolibri.xml.empresas.EmpresaDocument.Empresa;

public class Example {

	public Example(){

		//carregou a licenca?
		if (loadLicence())

		//processou o login?
		if (processLogin()){

			//acede a funcionalidades da API
			openViews();

		}

	}


	/**
	 * Carrega a licenca FREE
	 */
	public boolean loadLicence() {
		try{
			return LicenceManager.loadLicence(
				FileVARS.LICENCE_FOLDER + "INTERNACIONAL.rcplicence", "0");

		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}


	/**
	 * Processa login
	 */
	public boolean processLogin() {
		try{
			LoginAction login=new LoginAction(
				createEmpresa(),
				UserVARS.USER_ADMIN,
				UserVARS.USER_ADMIN_PASSWORD,
				WorkbenchHelper.getShell());

			return login.process();

		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}


	/**
	 * Abre vistas
	 */
	public void openViews() {
		try{

			//Vista de artigos
			//new OpenViewAction(ViewVARS.ArtigosFicheiroView).run();

			//Vista de emissao de documentos
			new OpenViewAction(ViewVARS.EntidadesEmissaoView,
				new Entidadesdocumentos(DatabaseManager.loadDocumentostipos("CFA")),
				CommandVARS.NOVO).run();

			//Pagina de preferencias - NAO DISPONIVEL
			//new OpenPreferencePageAction(WorkbenchHelper.getWorkbenchWindow()).run();

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


}
