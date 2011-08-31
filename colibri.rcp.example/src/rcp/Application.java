/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.dma.utils.eclipse.swt.ErrorHandler;
import org.dma.utils.java.Debug;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import rcp.colibri.RCPcolibri;
import rcp.colibri.core.login.ColibriLogin;
import rcp.colibri.core.security.LicenceManager;
import rcp.colibri.core.vars.database.DatanucleusVARS;
import rcp.colibri.dao.xmlbeans.EmpresasXml;
import rcp.colibri.workbench.shells.login.LoginShell;
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
			 * Verificacao interna usada no desenvolvimento
			 * PODE SER REMOVIDA livremente
			 */
			if (RCPcolibri.checkPlugin())
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

				Debug.info("### COLIBRI LOGIN ###", "OK!");

				if (CREATE_WORKBENCH){
					/*
					 * Cria workbench atraves de ApplicationWorkbenchAdvisor
					 */
					if (PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor()) == PlatformUI.RETURN_RESTART)
						exitCode = IApplication.EXIT_RESTART;
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
	 * Processa login de forma manual
	 */
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


	/**
	 * Processa login de forma automatica
	 */
	public static boolean login() {

		ColibriLogin login=new ColibriLogin();

		try{

			if (LicenceManager.loadLicence(
				/*
				 * LICENCA
				 * A licenca deve ser carregada antes do login ser efectuado
				 * Por defeito e' carregada a licenca FREE (certificada)
				 */
				//FileVARS.LICENCE_FOLDER+"PORTUGAL.rcplicence", "0") &&
				"/colibri/licencas/PRO.rcplicence", "0") &&

				login.process(
				/*
				 * EMPRESA
				 * Existem duas formas de obter a empresa de login
				 * 1) Atraves do carregamento de uma empresa existente
				 * 2) Atraves da criacao de um objecto empresa
				 */
				//EmpresasXml.load(0), // 1
				EmpresasXml.create(DatanucleusVARS.DRIVER_H2, "", "/colibri/rcp6", "sa", ""), // 2
				/*
				 * UTILIZADOR
				 * Por defeito e' assumido o utilizador ADMIN
				 */
				"admin", "admin"))
				return true;

		} catch (Exception e){
			e.printStackTrace();
		}

		ErrorHandler.show(login);

		return false;

	}


}
