/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import org.projectocolibri.api.Colibri.LICENCAS;
import org.projectocolibri.api.database.ColibriLogin;
import org.projectocolibri.api.database.ColibriLogin.RESULT;
import org.projectocolibri.rcp.importador.Activator;

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

		if (login()==RESULT.LOGIN) try{

			System.out.println("COLIBRI LOGIN OK!");

			if (PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor()) == PlatformUI.RETURN_RESTART){
				exitCode = IApplication.EXIT_RESTART;
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


	/** Login automatico */
	public static RESULT login() {

		try{
			//FACULTATIVO: a licenca e' carregada automaticamente
			LICENCAS.load("colibri12.rcplicence");

			ColibriLogin login=new ColibriLogin(0);
			//login do user ADMIN
			if (login.process("admin", "admin")) return RESULT.LOGIN;

			ErrorDialog.open(login.errors());

		}catch(Exception e){
			e.printStackTrace();
		}

		return RESULT.EXIT;

	}


}