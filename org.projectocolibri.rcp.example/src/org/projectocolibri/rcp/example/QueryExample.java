/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example;

import java.util.Collection;

import org.dma.java.utils.timedate.TimeChronograph;

import org.projectocolibri.rcp.colibri.core.vars.DatabaseVARS.FIELDS;
import org.projectocolibri.rcp.colibri.core.vars.DatabaseVARS.TABLES;
import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.filter.FilterMap;
import org.projectocolibri.rcp.colibri.dao.database.filter.FilterMap.OPERATORS;
import org.projectocolibri.rcp.colibri.dao.database.filter.FilterOperandMap;
import org.projectocolibri.rcp.colibri.dao.database.mapper.DatabaseFieldKey;
import org.projectocolibri.rcp.colibri.dao.database.populate.tables.DocumentostiposPopulate;
import org.projectocolibri.rcp.colibri.dao.jdo.query.QueryDefinition;
import org.projectocolibri.rcp.colibri.dao.jdo.query.QueryDefinition.QUERY_ORDER;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidades;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidadesdocumentos;

public class QueryExample {

	public QueryExample() {}


	public void showEntidades(Collection<Entidades> col) {

		for(Entidades entidade: col){
			try{
				System.out.println(entidade);
				//carrega a entidade COMPLETA
				Entidades entidade2=ColibriDatabase.loadEntidades(entidade.getKey());
				//apresenta todos os campos
				System.out.println(entidade2);

			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}


	/** Executa query */
	public Collection<Entidades> executeQuery() {

		QueryDefinition query=new QueryDefinition(TABLES.entidades, QUERY_ORDER.ASCENDING);

		//filtro da query
		query.getFilterMap().
			//nome comecado por "a"
			addRule(new DatabaseFieldKey(FIELDS.entidades_nome),
					new FilterOperandMap(OPERATORS.MATH.STARTS_WITH, "a")).
			//OU nome comenado por "b"
			addRule(new DatabaseFieldKey(FIELDS.entidades_nome),
					new FilterOperandMap(OPERATORS.MATH.STARTS_WITH, "b"),
					OPERATORS.LOGICAL.OR).
			//OU nome comenado por "c"
			addRule(new DatabaseFieldKey(FIELDS.entidades_nome),
					new FilterOperandMap(OPERATORS.MATH.STARTS_WITH, "c"),
					OPERATORS.LOGICAL.OR).
			//E morada contendo "rua"
			addRule(new DatabaseFieldKey(FIELDS.entidades_morada),
					new FilterOperandMap(OPERATORS.MATH.CONTAINS, "rua"));

		//ENTIDADES apenas com o campo KEY
		return ColibriDatabase.getEntidadesCollection(query);

	}


	public void executeQuery2() {

		TimeChronograph timer=new TimeChronograph();

		timer.start();
		FilterMap filterMap=new FilterMap(TABLES.entidadesdocumentos).
				addRule(new DatabaseFieldKey(FIELDS.entidadesdocumentos_tipodocumento, FIELDS.documentostipos_codigo),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, DocumentostiposPopulate.RECORDS.clientes_factura.codigo)).
				addRule(new DatabaseFieldKey(FIELDS.entidadesdocumentos_numeroauxiliar),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, "123"));
		QueryDefinition query=new QueryDefinition(filterMap, QUERY_ORDER.DESCENDING);

		//DOCUMENTOS apenas com o campo KEY
		Collection<Entidadesdocumentos> col=ColibriDatabase.getEntidadesdocumentosCollection(query);
		if (!col.isEmpty()){
			//carrega o PRIMEIRO documento encontrado
			Entidadesdocumentos documento=ColibriDatabase.loadEntidadesdocumentos(col.iterator().next().getKey());
			System.out.println(documento+" "+timer.toString());

			timer.reset();
			documento=ColibriDatabase.loadEntidadesdocumentos(DocumentostiposPopulate.RECORDS.clientes_factura.codigo, "123");
			System.out.println(documento+" "+timer.toString());

			timer.stop();
		}

	}

}
