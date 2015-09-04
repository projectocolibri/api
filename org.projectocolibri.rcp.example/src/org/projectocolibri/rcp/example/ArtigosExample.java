/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example;

import java.math.BigDecimal;
import java.util.Collection;

import org.dma.java.util.StringUtils;

import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.mapper.TableMap.FIELDS;
import org.projectocolibri.rcp.colibri.dao.database.model.Artigos;
import org.projectocolibri.rcp.colibri.dao.database.model.Artigosprecos;
import org.projectocolibri.rcp.colibri.dao.database.model.Artigosunidades;

public class ArtigosExample {

	public ArtigosExample() {}


	/** Cria um novo artigo */
	public Artigos createArtigo(String codigo, String descricao) {
		try{
			//cria objecto artigo
			Artigos artigo=new Artigos(codigo, descricao);

			//cria unidades de medida
			Artigosunidades unidade=artigo.createUnidades("UN", BigDecimal.ONE);
			unidade.setCodigobarras(StringUtils.randomNumbers(FIELDS.artigosunidades_codigobarras.size.size));
			artigo.addUnidades(unidade);

			unidade=artigo.createUnidades("DZ", BigDecimal.valueOf(12));
			unidade.setCodigobarras(StringUtils.randomNumbers(FIELDS.artigosunidades_codigobarras.size.size));
			artigo.addUnidades(unidade);

			//cria precos por unidade
			Artigosprecos preco=artigo.createPrecos("001", "UN");
			//precos IVA calculados automaticamente
			preco.setPrecopvp(BigDecimal.valueOf(100));
			artigo.addPrecos(preco);

			preco=artigo.createPrecos("001", "DZ");
			preco.setPrecopvp(BigDecimal.valueOf(200));
			artigo.addPrecos(preco);

			return artigo;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}



	/** Cria uma copia do artigo */
	public Artigos copyArtigo(Artigos artigo, String codigo, String descricao) {
		try{
			Artigos copy=artigo.cleanCopy();

			copy.setCodigo(codigo);
			copy.setDescricao(descricao);
			copy.setDescricao2("CLONE");

			return copy;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}



	/** Mostra a coleccao de artigos */
	public void showArtigos() {

		//ARTIGOS apenas com os campos RAIZ
		Collection<Artigos> col=ColibriDatabase.getArtigosCollection();

		for(Artigos e: col) try{

			System.out.println(e);
			//carrega o artigo completo
			Artigos artigo=ColibriDatabase.loadArtigos(e.getCodigo());
			//saca a primeira unidade medida
			System.out.println(artigo.getUnidades(0).getUnidademedida());
			//saca o primeiro preco
			System.out.println(artigo.getPrecos(0).getPrecopvp());

		}catch(Exception e1){
			e1.printStackTrace();
		}

	}


}
