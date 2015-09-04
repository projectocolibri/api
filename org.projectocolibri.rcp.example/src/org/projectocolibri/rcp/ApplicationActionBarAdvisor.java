/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp;

import org.dma.java.util.Debug;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import org.projectocolibri.rcp.colibri.workbench.ColibriUI;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		Debug.out();
		ColibriUI.initMenuBar(getActionBarConfigurer());
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		Debug.out();
		ColibriUI.initCoolBar(getActionBarConfigurer());
	}

	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		Debug.out();
		ColibriUI.initStatusBar(statusLine);
	}

}
