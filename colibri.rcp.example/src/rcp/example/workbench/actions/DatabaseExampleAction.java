/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench.actions;

import org.dma.utils.java.string.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.colibri.core.vars.model.SizeVARS;
import rcp.example.ArtigosExample;
import rcp.example.DocumentosExample;

public class DatabaseExampleAction extends Action implements IWorkbenchAction {

	public DatabaseExampleAction() {
		setText("#database");
	}


	public final void run(){

		/*
		 * Exemplos que PODEM correr COM e SEM workbench
		 */
		ArtigosExample artigosExample=new ArtigosExample();
		DocumentosExample documentosExample=new DocumentosExample();

		artigosExample.createArtigo(StringUtils.random(SizeVARS.artigos_codigo[0]),
				StringUtils.random(SizeVARS.artigos_descricao[0]/2));
		artigosExample.showArtigos();
		documentosExample.createDocumento("CFA",null);

	}

	public void dispose() {}


}
