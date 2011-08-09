/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example;

import org.dma.utils.java.Debug;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IPageLayout;

import rcpcolibri.core.ExceptionHandler;
import rcpcolibri.ui.ColibriUI;
import rcpcolibri.ui.workbench.ColibriCoolbar;
import rcpcolibri.ui.workbench.ColibriMenubar;
import rcpcolibri.ui.workbench.commands.OpenPreferencePageAction;
import rcpcolibri.ui.workbench.commands.ResetPerspectiveAction;
import rcpcolibri.ui.workbench.helpers.WorkbenchHack;
import rcpcolibri.ui.workbench.perspectives.IColibriPerspective;
import rcpcolibri.ui.workbench.views.actions.OpenViewAction;
import rcpcolibri.vars.gui.IconVARS;
import rcpcolibri.vars.gui.LabelVARS;
import rcpcolibri.vars.rcp.ViewVARS;

public class ExamplePerspective implements IColibriPerspective {

	public static final String ID = "ExamplePerspective";

	public void createInitialLayout(IPageLayout layout) {

		try {
			Debug.info(ID);

			layout.setEditorAreaVisible(false);
			layout.setFixed(true);

			//vista
			layout.addView(ViewVARS.ColibriView, IPageLayout.RIGHT, 0.75f, layout.getEditorArea());
			layout.getViewLayout(ViewVARS.ColibriView).setCloseable(false);

			layout.addPerspectiveShortcut(ID);
			WorkbenchHack.addAllPerspectiveShortcuts(layout);

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

	}


	public void buildCoolbar() {

		ColibriCoolbar coolbar=ColibriUI.getCoolbar();

		coolbar.add(ID, "Colibri",
			new IAction[]{
			new OpenPreferencePageAction(coolbar.getWindow())});

	}


	public void buildMenubar() {

		ColibriMenubar menubar=ColibriUI.getMenubar();

		OpenViewAction openViewAction=new OpenViewAction(ViewVARS.ArtigosFicheiroView);
		openViewAction.setText(LabelVARS.menu_artigos_ficheiro);
		openViewAction.setImageDescriptor(ColibriUI.getImageDescriptor(IconVARS.COOLBAR_ARTIGOS_FICHEIRO));

		menubar.add(ID,	LabelVARS.menubar_ficheiro,
			new IAction[]{
			new OpenPreferencePageAction(menubar.getWindow()),
			new ResetPerspectiveAction(menubar.getWindow()),
			openViewAction});

	}


}
