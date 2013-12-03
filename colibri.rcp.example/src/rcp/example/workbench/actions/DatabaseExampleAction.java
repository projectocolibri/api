/*******************************************************************************
 * 2008-2013 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example.workbench.actions;

import org.dma.java.utils.string.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import rcp.colibri.core.vars.DatabaseVARS.FIELDS;
import rcp.colibri.core.vars.PopulateVARS.DOCUMENTOSTIPOS;
import rcp.colibri.core.vars.PopulateVARS.ENTIDADESTIPOS;
import rcp.colibri.dao.model.classes.Artigos;
import rcp.colibri.dao.model.classes.Entidades;
import rcp.colibri.dao.model.classes.Entidadesdocumentos;
import rcp.example.ArtigosExample;
import rcp.example.DocumentosExample;
import rcp.example.EntidadesExample;
import rcp.example.QueryExample;

public class DatabaseExampleAction extends Action implements IWorkbenchAction {

	public DatabaseExampleAction() {
		setText("#database");
	}


	public final void run(){

		/*
		 * Exemplos que PODEM correr COM e SEM workbench
		 */
		QueryExample queryExample=new QueryExample();
		queryExample.executeQuery();

		/*
		 * Artigos
		 */
		ArtigosExample artigosExample=new ArtigosExample();
		Artigos artigo=artigosExample.createArtigo(
				StringUtils.random(FIELDS.artigos_codigo.size.size),
				StringUtils.random(FIELDS.artigos_descricao.size.size/2));
		artigosExample.showArtigos();

		/*
		 * Entidades
		 */
		EntidadesExample entidadesExample=new EntidadesExample();
		Entidades entidade=entidadesExample.createCliente(
				ENTIDADESTIPOS.cliente.codigo,
				StringUtils.random(FIELDS.entidades_nome.size.size/2));
		entidadesExample.showEntidades(ENTIDADESTIPOS.cliente.codigo);

		/*
		 * Documentos
		 */
		DocumentosExample documentosExample=new DocumentosExample();
		Entidadesdocumentos documento=documentosExample.createDocumento(
				DOCUMENTOSTIPOS.clientes_factura.codigo,
				entidade.getKey(), artigo.getCodigo());

	}

	public void dispose() {}


}
