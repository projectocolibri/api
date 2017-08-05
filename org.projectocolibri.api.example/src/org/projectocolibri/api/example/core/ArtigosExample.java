/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.api.example.core;

import java.math.BigDecimal;
import java.util.Collection;

import org.dma.java.util.StringUtils;

import org.projectocolibri.api.database.ColibriDatabase;
import org.projectocolibri.api.database.mapper.TableMap.FIELDS;
import org.projectocolibri.api.database.model.Artigos;
import org.projectocolibri.api.database.model.Artigosprecos;
import org.projectocolibri.api.database.model.Artigosunidades;

public class ArtigosExample {

	public void run() {

		try{
			Artigos artigo=storeArtigo();

			storeCopy(artigo.getCodigo());

			showArtigos();

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	/** Grava um novo artigo */
	public Artigos storeArtigo() {

		//cria um novo artigo
		Artigos artigo=createArtigo(
				StringUtils.random(FIELDS.artigos_codigo.size.size),
				StringUtils.randomLetters(FIELDS.artigos_descricao.size.size/2));

		//grava e apresenta possiveis erros
		ColibriDatabase.storeArtigos(artigo).print();

		return artigo;

	}


	/** Grava uma copia do artigo */
	public Artigos storeCopy(String artigo) {

		Artigos copy=createCopy(artigo,
				StringUtils.random(FIELDS.artigos_codigo.size.size),
				StringUtils.randomLetters(FIELDS.artigos_descricao.size.size/2));

		//grava e apresenta possiveis erros
		ColibriDatabase.storeArtigos(copy).print();

		return copy;

	}


	/** Cria uma copia do artigo */
	public Artigos createCopy(String artigo, String codigo, String descricao) {

		Artigos copy=ColibriDatabase.loadArtigos(artigo).cleanCopy();

		copy.setCodigo(codigo);
		copy.setDescricao(descricao);
		copy.setDescricao2("CLONE");

		return copy;

	}


	/** Cria um novo artigo */
	public Artigos createArtigo(String codigo, String descricao) {

		//cria objecto artigo
		Artigos artigo=new Artigos(codigo, descricao);

		//adiciona unidades de medida
		Artigosunidades unidade=artigo.addUnidades("UN", BigDecimal.ONE);
		unidade.setCodigobarras(StringUtils.randomNumbers(FIELDS.artigosunidades_codigobarras.size.size));

		BigDecimal factor=new BigDecimal(StringUtils.randomNumbers(2));
		unidade=artigo.addUnidades("KG", factor);
		unidade.setCodigobarras(StringUtils.randomNumbers(FIELDS.artigosunidades_codigobarras.size.size));

		//adiciona precos por unidade
		Artigosprecos preco=artigo.addPrecos("001", "UN");
		//precos IVA calculados automaticamente
		BigDecimal precopvp=new BigDecimal(StringUtils.randomNumbers(3));
		preco.setPrecopvp(precopvp);

		preco=artigo.addPrecos("001", "KG");
		preco.setPrecopvp(precopvp.multiply(factor));

		return artigo;

	}


	/** Mostra a coleccao de artigos */
	public void showArtigos() {

		//ARTIGOS apenas com os campos RAIZ
		Collection<Artigos> col=ColibriDatabase.getArtigosCollection();

		for(Artigos element: col) try{

			//carrega ARTIGO completo
			Artigos artigo=ColibriDatabase.loadArtigos(element.getCodigo());

			System.out.println(artigo);
			//saca a primeira unidade medida
			System.out.println(artigo.getUnidades(0).getUnidademedida());
			//saca o primeiro preco
			System.out.println(artigo.getPrecos(0).getPrecopvp());

		}catch(Exception e){
			e.printStackTrace();
		}

	}


}
