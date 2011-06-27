/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example.api;

import rcpcolibri.dao.database.DatabaseManager;
import rcpcolibri.dao.model.classes.Entidadesdocumentos;
import rcpcolibri.ui.workbench.commands.OpenPreferencePageAction;
import rcpcolibri.ui.workbench.helpers.WorkbenchHelper;
import rcpcolibri.ui.workbench.views.IView;
import rcpcolibri.ui.workbench.views.actions.OpenViewAction;
import rcpcolibri.vars.rcp.CommandVARS;
import rcpcolibri.vars.rcp.ViewVARS;

public class ViewsExample {

	public ViewsExample(){
	}


	/**
	 * Abre vista de artigos
	 */
	public IView openArtigos() {
		try{

			OpenViewAction action=new OpenViewAction(ViewVARS.ArtigosFicheiroView);

			action.run();

			return action.getView();

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * Abre vista de documentos
	 */
	public IView openDocumentos() {
		try{

			OpenViewAction action=new OpenViewAction(ViewVARS.EntidadesEmissaoView,
				new Entidadesdocumentos(DatabaseManager.loadDocumentostipos("CFA")), CommandVARS.NOVO);

			action.run();

			return action.getView();

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * Abre pagina de preferencias
	 * NAO DISPONIVEL!
	 */
	public void openPreferences() {
		try{

			new OpenPreferencePageAction(WorkbenchHelper.getWorkbenchWindow()).run();

		}catch(Exception e){
			e.printStackTrace();
		}
	}


}
