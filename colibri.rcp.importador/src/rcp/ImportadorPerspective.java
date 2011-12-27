/*******************************************************************************
 * 2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package rcp;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ImportadorPerspective implements IPerspectiveFactory {

	public static final String ID = "ImportadorPerspective";

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
	}

}
