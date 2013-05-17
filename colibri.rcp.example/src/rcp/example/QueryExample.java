/*******************************************************************************
 * 2008-2013 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import java.util.Collection;
import java.util.Iterator;

import org.dma.java.utils.Debug;

import rcp.colibri.core.mappers.database.DatabaseFieldKey;
import rcp.colibri.core.mappers.filters.FilterMap;
import rcp.colibri.core.mappers.filters.FilterMap.OPERATORS;
import rcp.colibri.core.mappers.filters.FilterOperandMap;
import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.database.query.QueryDefinition;
import rcp.colibri.dao.database.query.QueryDefinition.QUERY_ORDER;
import rcp.colibri.dao.model.classes.Entidades;
import rcp.colibri.vars.database.DatabaseVARS.FIELDS;
import rcp.colibri.vars.database.DatabaseVARS.TABLES;

public class QueryExample {

	public QueryExample(){
	}

	/**
	 * Cria uma nova query
	 */
	public QueryDefinition createQuery() {

		FilterMap filterMap=new FilterMap(TABLES.entidades).
			//nome comecado por "a"
			addRule(new DatabaseFieldKey(FIELDS.entidades_nome),
					OPERATORS.MATH.STARTS_WITH, new FilterOperandMap("a")).
			//OU nome comenado por "b"
			addRule(new DatabaseFieldKey(FIELDS.entidades_nome),
					OPERATORS.MATH.STARTS_WITH, new FilterOperandMap("b"),
					OPERATORS.LOGICAL.OR).
			//OU nome comenado por "c"
			addRule(new DatabaseFieldKey(FIELDS.entidades_nome),
					OPERATORS.MATH.STARTS_WITH, new FilterOperandMap("c"),
					OPERATORS.LOGICAL.OR).
			//E morada contendo "rua"
			addRule(new DatabaseFieldKey(FIELDS.entidades_morada),
					OPERATORS.MATH.CONTAINS, new FilterOperandMap("rua"));

		return new QueryDefinition(filterMap,
				new DatabaseFieldKey[]{new DatabaseFieldKey(FIELDS.entidades_key)},
				QUERY_ORDER.ASCENDING);

	}



	/**
	 * Executa a query
	 */
	public void executeQuery() {
		try{
			QueryDefinition queryDefinition=createQuery();

			Collection<Entidades> collection=ColibriDatabase.getCollectionEntidades(queryDefinition);

			Iterator<Entidades> iterator=collection.iterator();
			while(iterator.hasNext()){
				Entidades entidade=ColibriDatabase.loadEntidades(iterator.next().getKey());
				Debug.out(entidade);
			}

			queryDefinition.closeQuery();

		} catch (Exception e){
			e.printStackTrace();
		}

	}


}
