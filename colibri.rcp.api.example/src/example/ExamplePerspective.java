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
import rcpcolibri.ui.workbench.helpers.PerspectiveHelper;

public class ExamplePerspective implements IPerspectiveFactory {

	public static final String ID = "ExamplePerspective";

	public void createInitialLayout(IPageLayout layout) {

		try {
			Debug.info(ID);

			String editorArea = layout.getEditorArea();
			layout.setEditorAreaVisible(false);
			layout.setFixed(true);

			layout.addPerspectiveShortcut(ID);
			PerspectiveHelper.addAllPerspectiveShortcuts(layout);

			ColibriGUI.getCoolbar().put(ID, new ToolBarContributionItem[]{});
			ColibriGUI.getMenubar().put(ID, new LinkedHashMap());

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

	}


}
