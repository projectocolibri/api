/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example.api;

import rcpcolibri.core.ExceptionHandler;
import rcpcolibri.core.LoginAction;
import rcpcolibri.core.security.LicenceManager;
import rcpcolibri.vars.database.DatanucleusVARS;
import rcpcolibri.vars.rcp.FileVARS;
import rcpcolibri.vars.security.UserVARS;
import rcpcolibri.xml.empresas.DbcolibriDocument.Dbcolibri;
import rcpcolibri.xml.empresas.EmpresaDocument.Empresa;

public class LoginExample {

	/**
	 * Processa login
	 */
	public static boolean processLogin() {
		try{

			return loadLicence() && new LoginAction(
				createEmpresa(),
				UserVARS.USER_ADMIN,
				UserVARS.USER_ADMIN_PASSWORD).process();

		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}


	/**
	 * Carrega a licenca FREE
	 */
	public static boolean loadLicence() {
		try{

			return LicenceManager.loadLicence(
				FileVARS.LICENCE_FOLDER + "INTERNACIONAL.rcplicence", "0");

		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}


	private static Empresa createEmpresa(){
		try {

			Empresa empresa = Empresa.Factory.newInstance();
			empresa.setNome("Colibri");

			Dbcolibri dbcolibri = Dbcolibri.Factory.newInstance();
			dbcolibri.setDriver(DatanucleusVARS.DRIVER_H2);
			dbcolibri.setHost("");
			dbcolibri.setDatabase("/example.api/rcp6");
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
