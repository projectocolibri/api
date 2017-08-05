/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import org.projectocolibri.api.Colibri.LICENCAS;
import org.projectocolibri.api.Colibri.PREFERENCES;
import org.projectocolibri.api.database.ColibriLogin;
import org.projectocolibri.api.database.ColibriLogin.RESULT;
import org.projectocolibri.api.database.DatabaseParameters;
import org.projectocolibri.api.database.DatabaseParameters.DRIVERS;
import org.projectocolibri.rcp.magento.Activator;
import org.projectocolibri.ui.login.shell.LoginShell;

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

		Display display = PlatformUI.createDisplay();

		try{
			//desliga BACKUPS AUTOMATICOS
			PREFERENCES.AUTOMATIC_BACKUPS.value.setValue(false);
			/*
			 * LOGIN
			 * Existem duas formas de efectuar login
			 * NOTA: Comentar ou remover o metodo nao utilizado!
			 */
			//METODO 1: Atraves do ecra de login
			//if (login(display)==RESULT.LOGIN){}

			//METODO 2: Atraves do login automatico
			if (login()==RESULT.LOGIN){

				System.out.println("COLIBRI LOGIN OK!");

				if (PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor()) == PlatformUI.RETURN_RESTART){
					exitCode = IApplication.EXIT_RESTART;
				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			display.dispose();
		}

		return exitCode;

	}


	@Override
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null || !PlatformUI.isWorkbenchRunning()) return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable(){
			@Override
			public void run() {
				if(!display.isDisposed()) workbench.close();
			}
		});
	}


	/** Shell de login */
	private RESULT login(Display display) {

		try{
			//open and sleep
			return new LoginShell(display).getResult();

		}catch(Exception e){
			e.printStackTrace();
		}

		return RESULT.EXIT;

	}


	/** Login automatico */
	public static RESULT login() {

		try{
			//FACULTATIVO: a licenca e' carregada automaticamente
			LICENCAS.load("colibri12.rcplicence");

			//METODO 1: Atraves da criacao de parametros
			ColibriLogin login1=new ColibriLogin(new DatabaseParameters(
					DRIVERS.MySQL, "localhost", "colibri11", "root", "1234"));

			//METODO 2: Atraves de uma empresa existente
			ColibriLogin login2=new ColibriLogin(0);

			//login do user ADMIN
			if (login2.process("admin", "admin")) return RESULT.LOGIN;

			ErrorDialog.open(login2.errors());

		}catch(Exception e){
			e.printStackTrace();
		}

		return RESULT.EXIT;

	}


}
