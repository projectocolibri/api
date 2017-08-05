/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.api.example.core;

import java.util.Collection;

import org.projectocolibri.api.database.ColibriDatabase;
import org.projectocolibri.api.database.filter.FilterMap;
import org.projectocolibri.api.database.filter.FilterMap.OPERATORS;
import org.projectocolibri.api.database.filter.FilterOperandMap;
import org.projectocolibri.api.database.mapper.TableFieldKey;
import org.projectocolibri.api.database.mapper.TableMap.FIELDS;
import org.projectocolibri.api.database.mapper.TableMap.TABLES;
import org.projectocolibri.api.database.model.Documentostipos;
import org.projectocolibri.api.database.model.Entidades;
import org.projectocolibri.api.database.model.Entidadesdocumentos;
import org.projectocolibri.api.database.query.QueryDefinition;
import org.projectocolibri.api.database.query.QueryDefinition.QUERY_ORDER;

public class QueryExample {

	public void run() {

		try{
			showEntidades(queryEntidades());

			showDocumentos(queryDocumentos());

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	/** Executa query */
	public Collection<Entidades> queryEntidades() {

		QueryDefinition query=new QueryDefinition(TABLES.entidades, QUERY_ORDER.ASCENDING);

		//filtro da query
		query.getFilterMap().
			//nome comecado por "a"
			addRule(new TableFieldKey(FIELDS.entidades_nome),
					new FilterOperandMap(OPERATORS.MATH.STARTS_WITH, "a")).
			//OU nome comenado por "b"
			addRule(new TableFieldKey(FIELDS.entidades_nome),
					new FilterOperandMap(OPERATORS.MATH.STARTS_WITH, "b"),
					OPERATORS.LOGICAL.OR).
			//OU nome comenado por "c"
			addRule(new TableFieldKey(FIELDS.entidades_nome),
					new FilterOperandMap(OPERATORS.MATH.STARTS_WITH, "c"),
					OPERATORS.LOGICAL.OR).
			//E morada contendo "rua"
			addRule(new TableFieldKey(FIELDS.entidades_morada),
					new FilterOperandMap(OPERATORS.MATH.CONTAINS, "rua"));

		//ENTIDADES apenas com o campo KEY
		return ColibriDatabase.getEntidadesCollection(query);

	}


	public Collection<Entidadesdocumentos> queryDocumentos() {

		FilterMap filterMap=new FilterMap(TABLES.entidadesdocumentos).
				addRule(new TableFieldKey(FIELDS.entidadesdocumentos_tipodocumento, FIELDS.documentostipos_codigo),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, Documentostipos.RECORDS.clientes_factura.codigo)).
				addRule(new TableFieldKey(FIELDS.entidadesdocumentos_numeroauxiliar),
						new FilterOperandMap(OPERATORS.MATH.EQUAL, "123"));

		QueryDefinition query=new QueryDefinition(filterMap, QUERY_ORDER.DESCENDING).
				setOrdering(new TableFieldKey(FIELDS.entidadesdocumentos_seriedocumento, FIELDS.documentosseries_serie),
							new TableFieldKey(FIELDS.entidadesdocumentos_numerodocumento));

		//DOCUMENTOS apenas com o campo KEY
		return ColibriDatabase.getEntidadesdocumentosCollection(query);

	}


	/** Mostra a coleccao de entidades */
	public void showEntidades(Collection<Entidades> col) {

		for(Entidades element: col) try{

			//carrega ENTIDADE completa
			Entidades entidade=ColibriDatabase.loadEntidades(element.getKey());

			System.out.println(entidade);

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	/** Mostra a coleccao de documentos */
	public void showDocumentos(Collection<Entidadesdocumentos> col) {

		for(Entidadesdocumentos element: col) try{

			//carrega DOCUMENTO completo
			Entidadesdocumentos documento=ColibriDatabase.loadEntidadesdocumentos(element.getKey());

			System.out.println(documento);

		}catch(Exception e){
			e.printStackTrace();
		}

	}


}
