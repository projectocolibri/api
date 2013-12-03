/*******************************************************************************
 * 2008-2013 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import rcp.colibri.dao.database.ColibriLogin;
import rcp.colibri.workbench.shells.login.LoginShell;
import rcp.colibri.workbench.shells.login.LoginShell.LOGIN_ACTION;
import rcp.example.workbench.actions.DatabaseExampleAction;
import rcp.example.workbench.actions.ReportExampleAction;

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

	public Object start(IApplicationContext context) {

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
			//if (login(display)==LOGIN_ACTION.LOGIN){ // 1
			if (login()){ // 2

				System.out.println("COLIBRI LOGIN OK!");

				if (CREATE_WORKBENCH){
					/*
					 * Cria workbench atraves de ApplicationWorkbenchAdvisor
					 */
					if (PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor()) == PlatformUI.RETURN_RESTART){
						exitCode = IApplication.EXIT_RESTART;
					}
				}else{
					/*
					 * Exemplos que podem correr SEM workbench
					 */
					new DatabaseExampleAction().run();
					new ReportExampleAction().run();
				}

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


	/**
	 * Shell de login
	 */
	private LOGIN_ACTION login(Display display) {

		try{
			LoginShell shell = new LoginShell(display);
			shell.open();

			while (!shell.isDisposed())
				if (!display.readAndDispatch())
					display.sleep();

			return shell.getLoginAction();

		} catch (Exception e){
			e.printStackTrace();
		}

		return LOGIN_ACTION.EXIT;

	}


	/**
	 * Login automatico
	 *
	 * LICENCA
	 * A licenca deve ser carregada antes do login ser efectuado
	 * Por defeito e' carregada a licenca FREE (certificada)
	 *
	 * EMPRESA
	 * Existem duas formas de obter a empresa de login
	 * 1) Atraves da criacao uma nova empresa
	 * 2) Atraves de uma empresa existente
	 *
	 * UTILIZADOR
	 * Por defeito e' assumido o utilizador ADMIN
	 */
	public static boolean login() {

		ColibriLogin login=new ColibriLogin();

		try{
			if (
			//login.loadLicence(FOLDERS.LICENCES.name+"PORTUGAL.rcplicence") &&
			login.process(
				//login.getEmpresa(0)
				login.createEmpresa(
					"H2 Integrado" /*, DRIVERS.H2.name, "", "colibri9", "sa", ""*/),
					"admin", "admin"))

			return true;

		} catch (Exception e){
			e.printStackTrace();
		}

		ErrorDialog.open(login.getErrors());

		return false;

	}


}
