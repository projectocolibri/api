/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example.api.database;

import rcpcolibri.dao.database.DatabaseManager;
import rcpcolibri.dao.model.classes.Artigos;
import rcpcolibri.dao.model.classes.Artigosunidades;

public class ArtigosExample {

	public ArtigosExample(){
	}


	/**
	 * Grava um novo artigo
	 */
	public boolean storeArtigo() {
		try{
			Artigos artigo=createArtigo("1");

			return !DatabaseManager.storeArtigos(artigo,
				artigo.getUnidades(), artigo.getPrecos(), artigo.getComposicao(),
				artigo.getExistencias(), false).hasErrors();

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
