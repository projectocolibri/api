/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import org.projectocolibri.rcp.colibri.dao.database.ColibriLogin;
import org.projectocolibri.rcp.colibri.workbench.shells.login.LoginShell;
import org.projectocolibri.rcp.colibri.workbench.shells.login.LoginShell.RESULT;
import org.projectocolibri.rcp.magento.Activator;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {

		System.out.println(Activator.PLUGIN_ID+" APPLICATION <start>");

		Integer exitCode = IApplication.EXIT_OK;

		Display display = PlatformUI.createDisplay();

		try{
			/*
			 * LOGIN
			 * Existem duas formas de efectuar login
			 * 1) Atraves do ecra de login
			 * 2) Atraves do metodo de login
			 */
			//if (login(display)==RESULT.LOGIN){ // 1
			if (login()){ // 2

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
	public static boolean login() {

		try{
			ColibriLogin login=new ColibriLogin(0);

			if (//login.loadLicence(FOLDERS.LICENCES.name+"PORTUGAL.rcplicence") &&
			login.process("admin", "admin")) return true;

			ErrorDialog.open(login.getErrors());

		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}


}
