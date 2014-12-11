/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.importador;

import org.projectocolibri.rcp.colibri.workbench.support.views.actions.OpenViewAction;
import org.projectocolibri.rcp.importador.workbench.ImportadorView;

public class RCPImportador {

	public RCPImportador() {

		new OpenViewAction(ImportadorView.ID).run();

	}


}
