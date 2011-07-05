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

			/*
			 * Carrega todos os artigos da base de dados
			 * Apenas o campo CODIGO e' devolvido por defeito na coleccao
			 * E' necessario carregar o artigo para aceder aos outros campos
			 */
			Collection<Artigos> artigos=DatabaseManager.getArtigosCollection();

			//itera a coleccao de artigos
			Iterator<Artigos> iterator=artigos.iterator();
			while(iterator.hasNext()){

				//carrega o artigo da base de dados
				Artigos artigo=DatabaseManager.loadArtigos(iterator.next().getCodigo());

				//saca os campos do artigo
				System.out.println(artigo.getCodigo());
				System.out.println(artigo.getDescricao());
				System.out.println(artigo.getFamilia());

				//saca a primeira unidade de medida
				System.out.println(artigo.getUnidades().iterator().next().getUnidademedida());
				//saca o primeiro preco
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

			//grava o artigo na base de dados
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

			//cria objecto artigo
			Artigos artigo=new Artigos();
			//inicializa o objecto
			artigo.init(codigo);

			//adiciona uma unidade de medida
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

			//cria objecto unidade
			Artigosunidades unidades=new Artigosunidades();

			//inicializa o numero da linha (comeca em 1)
			unidades.setNumerolinha(artigo.getUnidades().size()+1);

			//inicializa a unidade de medida
			unidades.setUnidademedida(DatabaseManager.loadUnidadesmedida(codigo));

			return unidades;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


}
