/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.dma.utils.eclipse.swt.DialogHandler;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import rcp.colibri.core.login.ColibriLogin;
import rcp.colibri.core.security.LicenceManager;
import rcp.colibri.core.vars.rcp.FileVARS;
import rcp.colibri.dao.xmlbeans.EmpresasXml;
import rcp.colibri.workbench.shells.login.LoginShell;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	public Object start(IApplicationContext context) {

		System.out.println(Activator.PLUGIN_ID+" APPLICATION <start>");

		Integer exitCode = IApplication.EXIT_OK;

		Display display = PlatformUI.createDisplay();

		try{
			/*
			 * LOGIN
			 * Existem duas formas de efectuar login
			 * 1) De forma manual, atraves do ecra de login
			 * 2) De forma automatica, sem intervencao do utilizador
			 *
			 * NOTAS
			 * O metodo 1 permite editar as configuracoes de
			 * a) Empresas e base de dados
			 * b) Licenca a utilizar por defeito
			 * c) LINGUA (nao acessivel por via interna)
			 *
			 */
			//if (login(display)==LoginShell.ACTION_LOGIN){ // 1
			if (login()){ // 2

				System.out.println("COLIBRI LOGIN OK!");

				if (PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor()) == PlatformUI.RETURN_RESTART)
					exitCode = IApplication.EXIT_RESTART;

			}

		} catch (Exception e){
			e.printStackTrace();
		} finally {
			display.dispose();
		}

		System.out.println(Activator.PLUGIN_ID+" APPLICATION <exit>: "+exitCode);

		return exitCode;

	}


	public void stop() {

		System.out.println(Activator.PLUGIN_ID+" APPLICATION <stop>");
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}


	private int login(Display display) {

		try{
			LoginShell shell = new LoginShell(display);
			shell.open();
			shell.setFocus();

			while (!shell.isDisposed())
				if (!display.readAndDispatch())
					display.sleep();

			return shell.getExitAction();

		} catch (Exception e){
			e.printStackTrace();
		}

		return LoginShell.ACTION_EXIT;

	}


	public static boolean login() {

		ColibriLogin login=new ColibriLogin();

		try{

			if (LicenceManager.loadLicence(
				FileVARS.LICENCE_FOLDER+"INTERNACIONAL.rcplicence", "0") &&
				login.process(EmpresasXml.load(0), "admin", "admin"))
				return true;

		} catch (Exception e){
			e.printStackTrace();
		}

		DialogHandler.error(login.getErrors());

		return false;

	}


}
