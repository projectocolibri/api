/*******************************************************************************
 * 2008-2013 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import java.util.Collection;
import java.util.Iterator;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.dma.java.utils.array.ErrorList;

import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.model.classes.Entidades;

public class EntidadesExample {

	public EntidadesExample(){
	}


	/**
	 * Cria uma nova entidade
	 */
	public Entidades createCliente(String tipoentidade, String nome) {
		try{
			//cria objecto entidade
			Entidades entidade=new Entidades(
				ColibriDatabase.loadEntidadestipos(tipoentidade),
				ColibriDatabase.getNextNumeroEntidades(tipoentidade));

			entidade.setNome(nome);

			//grava a entidade na base de dados
			ErrorList error=ColibriDatabase.storeEntidades(entidade,false);

			//apresenta possiveis erros
			ErrorDialog.open(error.getErrors());

			return entidade;

		} catch (Exception e){
			e.printStackTrace();
		}

		return null;

	}



	/**
	 * Mostra a coleccao de entidades
	 */
	public void showEntidades(String tipoentidade) {
		try{
			/*
			 * Carrega todas as entidades da base de dados
			 * Apenas os campos RAIZ sao devolvidos por defeito na coleccao
			 * E' necessario carregar a entidade para aceder aos outros campos
			 */
			Collection<Entidades> entidades=ColibriDatabase.getCollectionEntidades(tipoentidade);

			//itera a coleccao de entidades
			Iterator<Entidades> iterator=entidades.iterator();
			while(iterator.hasNext()){

				//carrega a entidade da base de dados
				Entidades entidade=ColibriDatabase.loadEntidades(iterator.next().getKey());

				System.out.println(entidade);

			}

		} catch (Exception e){
			e.printStackTrace();
		}

	}


}