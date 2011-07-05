/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example.api.database;

import java.util.Collection;
import java.util.Iterator;

import rcpcolibri.dao.database.DatabaseManager;
import rcpcolibri.dao.model.classes.Artigos;
import rcpcolibri.dao.model.classes.Artigosunidades;

public class ArtigosExample {

	public ArtigosExample(){
	}


	/**
	 * Itera a coleccao de artigos
	 */
	public void iterateArtigo() {
		try{

			Collection<Artigos> artigos=DatabaseManager.getArtigosCollection();

			Iterator<Artigos> iterator=artigos.iterator();
			while(iterator.hasNext()){

				/*
				 * Apenas o campo CHAVE e' devolvido por defeito na coleccao
				 * E' necessario carregar o artigo para aceder aos outros campos
				 */
				Artigos artigo=DatabaseManager.loadArtigos(iterator.next().getCodigo());

				System.out.println(artigo.getCodigo());
				System.out.println(artigo.getDescricao());
				System.out.println(artigo.getFamilia());
				//primeiro elemento
				System.out.println(artigo.getUnidades().iterator().next().getUnidademedida());
				//primeiro elemento
				System.out.println(artigo.getPrecos().iterator().next().getPrecopvp());

			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	/**
	 * Grava um novo artigo
	 */
	public boolean storeArtigo() {
		try{

			Artigos artigo=createArtigo("1");

			return !DatabaseManager.storeArtigos(artigo,
				artigo.getUnidades(),
				artigo.getPrecos(),
				artigo.getComposicao(),
				artigo.getExistencias(),
				false).hasErrors();

		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}


	/**
	 * Cria um novo artigo
	 */
	public Artigos createArtigo(String codigo) {
		try{

			Artigos artigo=new Artigos();
			artigo.init(codigo);

			artigo.addUnidades(createUnidademedida(artigo, "PK"));

			return artigo;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


	/**
	 * Cria uma nova unidade de medida
	 */
	public Artigosunidades createUnidademedida(Artigos artigo, String codigo) {
		try{

			Artigosunidades unidades=new Artigosunidades();
			//linha comeca em 1
			unidades.setNumerolinha(artigo.getUnidades().size()+1);
			unidades.setUnidademedida(DatabaseManager.loadUnidadesmedida(codigo));

			return unidades;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


}
