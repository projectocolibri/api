/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.api.example.actions;

import org.eclipse.jface.action.Action;

import org.projectocolibri.api.example.core.ArtigosExample;
import org.projectocolibri.api.example.core.DocumentosExample;
import org.projectocolibri.api.example.core.EntidadesExample;
import org.projectocolibri.api.example.core.QueryExample;

public class DatabaseExampleAction extends Action {

	public DatabaseExampleAction() {
		setText("#database");
	}


	@Override
	public final void run() {

		new QueryExample().run();
		new ArtigosExample().run();
		new EntidadesExample().run();
		new DocumentosExample().run();

	}


}
