/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.api;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import org.projectocolibri.api.Colibri.LICENCAS;
import org.projectocolibri.api.database.ColibriLogin;
import org.projectocolibri.api.database.ColibriLogin.RESULT;
import org.projectocolibri.api.database.DatabaseParameters;
import org.projectocolibri.api.database.DatabaseParameters.DRIVERS;
import org.projectocolibri.api.example.Activator;
import org.projectocolibri.api.example.actions.DatabaseExampleAction;
import org.projectocolibri.api.example.actions.ReportExampleAction;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	public Application() {
		System.err.println(Activator.PLUGIN_ID+"(APPLICATION)");
	}

	@Override
	public Object start(IApplicationContext context) throws Exception {

		Integer exitCode = IApplication.EXIT_OK;

		//login automatico
		if (login()==RESULT.LOGIN) try{

			System.out.println("COLIBRI LOGIN OK!");
			new DatabaseExampleAction().run();
			new ReportExampleAction().run();

		}catch(Exception e){
			e.printStackTrace();
		}finally{}

		return exitCode;

	}


	@Override
	public void stop() {}


	/** Login automatico */
	public static RESULT login() {

		try{
			//FACULTATIVO: a licenca e' carregada automaticamente
			LICENCAS.load("colibri12.rcplicence");

			//METODO 1: Atraves da criacao de parametros
			ColibriLogin login1=new ColibriLogin(new DatabaseParameters(
					DRIVERS.MySQL, "localhost", "colibri10", "root", "1234"));

			//METODO 2: Atraves de uma empresa existente
			ColibriLogin login2=new ColibriLogin(0);

			//login do user ADMIN
			if (login2.process("admin", "admin")) return RESULT.LOGIN;

			login2.errors().print();

		}catch(Exception e){
			e.printStackTrace();
		}

		return RESULT.EXIT;

	}


}