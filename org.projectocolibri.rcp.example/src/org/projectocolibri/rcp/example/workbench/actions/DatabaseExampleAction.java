/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.api.IconVARS;
import org.projectocolibri.api.example.core.ArtigosExample;
import org.projectocolibri.api.example.core.DocumentosExample;
import org.projectocolibri.api.example.core.EntidadesExample;
import org.projectocolibri.api.example.core.QueryExample;
import org.projectocolibri.rcp.colibri.workbench.support.ColibriAction;

public class DatabaseExampleAction extends ColibriAction implements IWorkbenchAction {

	public DatabaseExampleAction() {
		setText("#database");
		setImageDescriptor(IconVARS.TOOLBAR_PROCESSAR);
	}


	@Override
	public final void run() {

		new QueryExample().run();
		new ArtigosExample().run();
		new EntidadesExample().run();
		new DocumentosExample().run();

	}

	@Override
	public void dispose() {}


}
