/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import org.projectocolibri.rcp.colibri.workbench.shells.login.LoginShell;
import org.projectocolibri.rcp.colibri.workbench.shells.login.LoginShell.RESULT;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	public static final boolean CREATE_WORKBENCH=true;

	@Override
	public Object start(IApplicationContext context) throws Exception {

		System.out.println(Activator.PLUGIN_ID+" APPLICATION <start>");
		Integer exitCode = IApplication.EXIT_OK;
		Display display = PlatformUI.createDisplay();

		try{
			if (new LoginShell(display).getResult()==RESULT.LOGIN){
				if (CREATE_WORKBENCH && PlatformUI.createAndRunWorkbench(display,
					new ApplicationWorkbenchAdvisor()) == PlatformUI.RETURN_RESTART){
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

		if (!PlatformUI.isWorkbenchRunning()) return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed()) workbench.close();
			}
		});

	}

}