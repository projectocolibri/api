/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.importador;

import rcp.colibri.workbench.support.views.OpenViewAction;
import rcp.importador.workbench.ImportadorView;

public class RCPImportador {

	public RCPImportador() {

		new OpenViewAction(ImportadorView.ID).run();

	}


}
