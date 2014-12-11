/*******************************************************************************
 * 2008-2013 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import java.util.Collection;

import org.dma.java.utils.string.StringUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.rcp.colibri.core.vars.DatabaseVARS.FIELDS;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.DocumentostiposPopulate;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.EntidadestiposPopulate;
import org.projectocolibri.rcp.colibri.dao.model.classes.Artigos;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidades;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidadesdocumentos;
import org.projectocolibri.rcp.example.ArtigosExample;
import org.projectocolibri.rcp.example.DocumentosExample;
import org.projectocolibri.rcp.example.EntidadesExample;
import org.projectocolibri.rcp.example.QueryExample;

public class DatabaseExampleAction extends Action implements IWorkbenchAction {

	public DatabaseExampleAction() {
		setText("#database");
	}


	public final void run(){

		/*
		 * Exemplos que PODEM correr COM e SEM workbench
		 */
		QueryExample queryExample=new QueryExample();
		Collection<Entidades> col=queryExample.executeQuery();
		queryExample.showEntidades(col);
		queryExample.executeQuery2();

		/*
		 * Artigos
		 */
		ArtigosExample artigosExample=new ArtigosExample();
		Artigos artigo=artigosExample.createArtigo(
				StringUtils.randomLetters(FIELDS.artigos_codigo.size.size),
				StringUtils.randomLetters(FIELDS.artigos_descricao.size.size/2));
		artigosExample.showArtigos();

		/*
		 * Entidades
		 */
		EntidadesExample entidadesExample=new EntidadesExample();
		Entidades entidade=entidadesExample.createCliente(
				EntidadestiposPopulate.RECORDS.cliente.codigo,
				StringUtils.randomLetters(FIELDS.entidades_nome.size.size/2));
		entidadesExample.showEntidades(EntidadestiposPopulate.RECORDS.cliente.codigo);

		/*
		 * Documentos
		 */
		DocumentosExample documentosExample=new DocumentosExample();
		Entidadesdocumentos documento=documentosExample.createDocumento(
				DocumentostiposPopulate.RECORDS.clientes_factura.codigo,
				entidade.getKey(), artigo.getCodigo());

	}

	@Override
	public void dispose() {}


}
