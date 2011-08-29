/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench.actions;

import org.dma.utils.java.Debug;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.colibri.core.handlers.ExceptionHandler;
import rcp.example.ArtigosExample;
import rcp.example.DocumentosExample;

public class DatabaseExampleAction extends Action implements IWorkbenchAction {

	public DatabaseExampleAction() {
		setText("#database");
	}


	public final void run(){

		Debug.info(getText());

		try{
			/*
			 * Exemplos que PODEM correr COM e SEM workbench
			 */
			ArtigosExample artigosExample=new ArtigosExample();
			DocumentosExample documentosExample=new DocumentosExample();

			artigosExample.createArtigo("1","Produto");
			artigosExample.showArtigos();
			documentosExample.createDocumento("CFA",null);

		} catch (Exception e){
			ExceptionHandler.error(e);
		}
	}

	public void dispose() {}


}
