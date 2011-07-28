/*******************************************************************************
 * 2008-2010 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example;

import java.util.LinkedHashMap;

import org.dma.utils.java.Debug;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rcpcolibri.core.ExceptionHandler;
import rcpcolibri.ui.ColibriGUI;
import rcpcolibri.ui.workbench.helpers.WorkbenchHack;

public class ExamplePerspective implements IPerspectiveFactory {

	public static final String ID = "ExamplePerspective";

	public void createInitialLayout(IPageLayout layout) {

		try {
			Debug.info(ID);

			layout.setEditorAreaVisible(false);
			layout.setFixed(true);

			layout.addPerspectiveShortcut(ID);
			WorkbenchHack.addAllPerspectiveShortcuts(layout);

			ColibriGUI.getCoolbar().put(ID, new ToolBarContributionItem[]{});
			ColibriGUI.getMenubar().put(ID, new LinkedHashMap());

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

	}


}
