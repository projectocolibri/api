/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import java.util.Collection;
import java.util.Iterator;

import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.model.classes.Artigos;

public class ArtigosExample {

	public ArtigosExample(){
	}


	/**
	 * Grava um artigo
	 */
	public boolean storeArtigo(Artigos artigo, boolean edited) {
		try{

			//grava o artigo na base de dados
			return !ColibriDatabase.storeArtigos(artigo,
				artigo.getUnidades(),
				artigo.getPrecos(),
				artigo.getComposicao(),
				artigo.getExistencias(),
				edited).hasErrors();

		} catch (Exception e){
			e.printStackTrace();
		}

		return false;

	}


	/**
	 * Cria um novo artigo
	 */
	public void createArtigo(String codigo, String descricao) {
		try{

			//cria objecto artigo
			Artigos artigo=new Artigos(codigo);
			artigo.setDescricao(descricao);

			//adiciona uma unidade de medida
			artigo.addUnidades(artigo.createUnidades("PK"));
			//adiciona um preco
			artigo.addPrecos(artigo.createPrecos("001"));

			storeArtigo(artigo, false);

		} catch (Exception e){
			e.printStackTrace();
		}

	}



	/**
	 * Mostra a coleccao de artigos
	 */
	public void showArtigos() {
		try{

			/*
			 * Carrega todos os artigos da base de dados
			 * Apenas o campo CODIGO e' devolvido por defeito na coleccao
			 * E' necessario carregar o artigo para aceder aos outros campos
			 */
			Collection<Artigos> artigos=ColibriDatabase.getArtigosCollection();

			//itera a coleccao de artigos
			Iterator<Artigos> iterator=artigos.iterator();
			while(iterator.hasNext()){

				//carrega o artigo da base de dados
				Artigos artigo=ColibriDatabase.loadArtigos(iterator.next().getCodigo());

				System.out.println(artigo.getCodigo()+" - "+artigo.getDescricao());
				//saca a primeira unidade de medida
				System.out.println(artigo.getUnidades().iterator().next().getUnidademedida());
				//saca o primeiro preco
				System.out.println(artigo.getPrecos().iterator().next().getPrecopvp());

			}

		} catch (Exception e){
			e.printStackTrace();
		}

	}


}
