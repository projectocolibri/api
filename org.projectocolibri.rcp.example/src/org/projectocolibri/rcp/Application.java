/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import org.projectocolibri.rcp.colibri.RCPcolibri.PREFERENCES;
import org.projectocolibri.rcp.colibri.dao.database.ColibriLogin;
import org.projectocolibri.rcp.colibri.dao.database.DatabaseParameters;
import org.projectocolibri.rcp.colibri.dao.database.DatabaseParameters.DRIVERS;
import org.projectocolibri.rcp.colibri.workbench.shells.login.LoginShell;
import org.projectocolibri.rcp.colibri.workbench.shells.login.LoginShell.RESULT;
import org.projectocolibri.rcp.example.Activator;
import org.projectocolibri.rcp.example.workbench.actions.DatabaseExampleAction;
import org.projectocolibri.rcp.example.workbench.actions.ReportExampleAction;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/*
	 * Alterar CREATE_WORKBENCH para testar COM e SEM workbench
	 *
	 * Caso o workbench nao seja necessario, as classes que
	 * configuram a plataforma RCP podem ser removidas:
	 *
	 * ApplicationWorkbenchAdvisor
	 * ApplicationWorkbenchWindowAdvisor
	 * ApplicationActionBarAdvisor
	 *
	 * Informacao sobre a plataforma RCP
	 * http://www.eclipse.org/articles/Article-RCP-1/tutorial1.html
	 *
	 */
	public static final boolean CREATE_WORKBENCH=true;

	@Override
	public Object start(IApplicationContext context) throws Exception {

		System.out.println(Activator.PLUGIN_ID+" APPLICATION <start>");

		Integer exitCode = IApplication.EXIT_OK;

		Display display = PlatformUI.createDisplay();

		try{
			//desliga BACKUPS AUTOMATICOS
			PREFERENCES.AUTOMATIC_BACKUPS.setValue(false);
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

				if (CREATE_WORKBENCH){
					//Cria workbench atraves de ApplicationWorkbenchAdvisor
					if (PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor()) == PlatformUI.RETURN_RESTART){
						exitCode = IApplication.EXIT_RESTART;
					}
				}else{
					//Exemplos que podem correr SEM workbench
					new DatabaseExampleAction().run();
					new ReportExampleAction().run();
				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			display.dispose();
		}

		System.out.println(Activator.PLUGIN_ID+" APPLICATION <exit>: "+exitCode);

		return exitCode;

	}


	@Override
	public void stop() {

		System.out.println(Activator.PLUGIN_ID+" APPLICATION <stop>");
		if (!PlatformUI.isWorkbenchRunning()) return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.isDisposed()) workbench.close();
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
			//METODO 1: Atraves da criacao de parametros
			ColibriLogin login1=new ColibriLogin(new DatabaseParameters(
					DRIVERS.MySQL, "localhost", "colibri10", "root", "1234"));

			//METODO 2: Atraves de uma empresa existente
			ColibriLogin login2=new ColibriLogin(0);

			//FACULTATIVO: a licenca e' carregada automaticamente
			login2.loadLicence("colibri11.rcplicence");
			//login do user ADMIN
			if (login2.process("admin", "admin")) return RESULT.LOGIN;

			ErrorDialog.open(login2.getErrors());

		}catch(Exception e){
			e.printStackTrace();
		}

		return RESULT.EXIT;

	}


}