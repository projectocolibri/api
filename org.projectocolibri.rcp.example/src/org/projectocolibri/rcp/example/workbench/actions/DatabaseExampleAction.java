/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example.workbench.actions;

import java.util.Collection;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.dma.java.util.StringUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.mapper.TableMap.FIELDS;
import org.projectocolibri.rcp.colibri.dao.database.model.Artigos;
import org.projectocolibri.rcp.colibri.dao.database.model.Entidades;
import org.projectocolibri.rcp.colibri.dao.database.model.Entidadesdocumentos;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.DocumentostiposPopulate;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.EntidadestiposPopulate;
import org.projectocolibri.rcp.example.ArtigosExample;
import org.projectocolibri.rcp.example.DocumentosExample;
import org.projectocolibri.rcp.example.EntidadesExample;
import org.projectocolibri.rcp.example.QueryExample;

public class DatabaseExampleAction extends Action implements IWorkbenchAction {

	public DatabaseExampleAction() {
		setText("#database");
	}


	@Override
	public final void run() {

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
		//cria um novo artigo
		Artigos artigo=artigosExample.createArtigo(
				StringUtils.random(FIELDS.artigos_codigo.size.size),
				StringUtils.randomLetters(FIELDS.artigos_descricao.size.size/2));
		//grava e apresenta possiveis erros
		ErrorDialog.open(ColibriDatabase.storeArtigos(artigo, false).getErrors());
		//cria uma copia do artigo
		Artigos copy=artigosExample.copyArtigo(
				ColibriDatabase.loadArtigos(artigo.getCodigo()),
				StringUtils.random(FIELDS.artigos_codigo.size.size),
				StringUtils.randomLetters(FIELDS.artigos_descricao.size.size/2));
		//grava e apresenta possiveis erros
		ErrorDialog.open(ColibriDatabase.storeArtigos(copy, false).getErrors());
		artigosExample.showArtigos();

		/*
		 * Entidades
		 */
		EntidadesExample entidadesExample=new EntidadesExample();
		//cria uma nova entidade
		Entidades entidade=entidadesExample.createCliente(
				EntidadestiposPopulate.RECORDS.cliente.codigo,
				StringUtils.randomLetters(FIELDS.entidades_nome.size.size/2));
		//grava e apresenta possiveis erros
		ErrorDialog.open(ColibriDatabase.storeEntidades(entidade,false).getErrors());
		entidadesExample.showEntidades(EntidadestiposPopulate.RECORDS.cliente.codigo);

		/*
		 * Documentos
		 */
		DocumentosExample documentosExample=new DocumentosExample();
		//cria um novo documento
		Entidadesdocumentos documento=documentosExample.createDocumento(
				DocumentostiposPopulate.RECORDS.clientes_factura.codigo,
				entidade.getKey(), artigo.getCodigo());
		//grava e apresenta possiveis erros
		ErrorDialog.open(ColibriDatabase.storeEntidadesdocumentos(documento, false).getErrors());

	}

	@Override
	public void dispose() {}


}
