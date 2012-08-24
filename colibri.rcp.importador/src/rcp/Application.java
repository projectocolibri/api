/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import rcp.colibri.workbench.shells.login.LoginShell;

public class Application implements IApplication {

	public static final boolean CREATE_WORKBENCH=true;

	public Object start(IApplicationContext context) {

		System.out.println(rcp.Activator.PLUGIN_ID+" APPLICATION <start>");
		Integer exitCode = IApplication.EXIT_OK;
		Display display = PlatformUI.createDisplay();

		try{
			if (login(display)==LoginShell.ACTION_LOGIN){

				if (CREATE_WORKBENCH){
					if (PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor()) == PlatformUI.RETURN_RESTART)
						exitCode = IApplication.EXIT_RESTART;
				}
			}

		} catch (Exception e){
			e.printStackTrace();
		} finally {
			display.dispose();
		}

		System.out.println(rcp.Activator.PLUGIN_ID+" APPLICATION <exit>: "+exitCode);

		return exitCode;
	}


	public void stop() {
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

			while (!shell.isDisposed())
				if (!display.readAndDispatch())
					display.sleep();

			return shell.getExitAction();

		} catch (Exception e){
			e.printStackTrace();
		}

		return LoginShell.ACTION_EXIT;

	}

}