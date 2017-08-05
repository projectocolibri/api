/*******************************************************************************
 * 2008-2016 Projecto Colibri
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
import org.projectocolibri.rcp.colibri.workbench.support.bars.coolbar.ColibriCoolBar;
import org.projectocolibri.rcp.colibri.workbench.support.bars.menubar.ColibriMenuBar;
import org.projectocolibri.rcp.colibri.workbench.support.bars.statusbar.ColibriStatusBar;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		Debug.out();
		ColibriUI.setMenuBar(new ColibriMenuBar(getActionBarConfigurer()));
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		Debug.out();
		ColibriUI.setCoolBar(new ColibriCoolBar(getActionBarConfigurer()));
	}

	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		Debug.out();
		ColibriUI.setStatusBar(new ColibriStatusBar(statusLine));
	}

}
