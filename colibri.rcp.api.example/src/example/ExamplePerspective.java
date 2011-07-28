/*******************************************************************************
 * 2008-2010 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example;

import java.util.LinkedHashMap;
import java.util.Map;

import org.dma.utils.java.Debug;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rcpcolibri.core.ExceptionHandler;
import rcpcolibri.ui.ColibriGUI;
import rcpcolibri.ui.workbench.ColibriCoolbar;
import rcpcolibri.ui.workbench.ColibriMenubar;
import rcpcolibri.ui.workbench.commands.OpenPreferencePageAction;
import rcpcolibri.ui.workbench.helpers.WorkbenchHack;
import rcpcolibri.vars.gui.IconVARS;
import rcpcolibri.vars.gui.LabelVARS;

public class ExamplePerspective implements IPerspectiveFactory {

	public static final String ID = "ExamplePerspective";

	public void createInitialLayout(IPageLayout layout) {

		try {
			Debug.info(ID);

			layout.setEditorAreaVisible(false);
			layout.setFixed(true);

			layout.addPerspectiveShortcut(ID);
			WorkbenchHack.addAllPerspectiveShortcuts(layout);

			/*
			 * A perspectiva e' responsavel pela criacao das TOOLBAR e MENUBAR
			 * Uma vez que a contribuicao tem de ser efectuada em TEMPO REAL
			 * na criacao da perspectiva, nao existe a possibilidade de efectuar
			 * o SAVE & RESTORE da mesma
			 */
			buildCoolbar();
			buildMenubar();

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

	}

	public void buildCoolbar() {

		try {
			ColibriCoolbar coolbar=ColibriGUI.getCoolbar();

			//Preferencias
			OpenPreferencePageAction preferencePageAction=new OpenPreferencePageAction(coolbar.getWindow());
			preferencePageAction.setToolTipText(LabelVARS.toolbar_preferencias);
			preferencePageAction.setImageDescriptor(ColibriGUI.getImageDescriptor(IconVARS.COOLBAR_PARAMETROS));
			coolbar.getConfigurer().registerGlobalAction(preferencePageAction);

			//Toolbar
			IToolBarManager toolbar=new ToolBarManager(SWT.FLAT);
			toolbar.add(preferencePageAction);

			coolbar.put(ID, new ToolBarContributionItem[]{
				new ToolBarContributionItem(toolbar,ID)});

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

	}


	public void buildMenubar() {

		try {
			ColibriMenubar menubar=ColibriGUI.getMenubar();

			Map<String, IAction[]> actions=new LinkedHashMap();
			actions.put(LabelVARS.menubar_ficheiro,	new IAction[]{
				new OpenPreferencePageAction(menubar.getWindow())});

			menubar.put(ID, actions);

		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

	}


}
