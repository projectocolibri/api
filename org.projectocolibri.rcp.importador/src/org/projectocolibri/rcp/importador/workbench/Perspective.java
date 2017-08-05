/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.importador.workbench;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "Perspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(false);
		layout.setFixed(true);

		layout.addStandaloneView(View.ID, false, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
		layout.getViewLayout(View.ID).setCloseable(false);

	}


}
