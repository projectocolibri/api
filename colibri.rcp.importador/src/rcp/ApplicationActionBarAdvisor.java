/*******************************************************************************
 * 2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.dma.utils.java.Debug;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import rcp.colibri.workbench.ColibriUI;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}


	protected void fillMenuBar(IMenuManager menuBar) {
		Debug.info();
		ColibriUI.initMenubar(getActionBarConfigurer());
	}


	protected void fillCoolBar(ICoolBarManager coolbar) {
		Debug.info();
		ColibriUI.initCoolbar(getActionBarConfigurer());
	}


	protected void fillStatusLine(IStatusLineManager statusline) {
		Debug.info();
		ColibriUI.initStatusbar(statusline);
	}


}