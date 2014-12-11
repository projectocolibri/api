/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example;

import java.math.BigDecimal;
import java.util.Collection;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.dma.java.utils.array.ErrorList;
import org.dma.java.utils.string.StringUtils;

import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.model.classes.Artigos;
import org.projectocolibri.rcp.colibri.dao.model.classes.Artigosprecos;
import org.projectocolibri.rcp.colibri.dao.model.classes.Artigosunidades;

public class ArtigosExample {

	public ArtigosExample() {}


	/** Cria um novo artigo */
	public Artigos createArtigo(String codigo, String descricao) {
		try{
			//cria objecto artigo
			Artigos artigo=new Artigos(codigo,descricao);

			//cria unidades de medida
			Artigosunidades unidade1=artigo.createUnidades("UN", BigDecimal.ONE);
			Artigosunidades unidade2=artigo.createUnidades("DZ", BigDecimal.valueOf(12));
			unidade1.setCodigobarras(StringUtils.randomNumbers(50));
			unidade2.setCodigobarras(StringUtils.randomNumbers(13));
			artigo.addUnidades(unidade1);
			artigo.addUnidades(unidade2);

			//cria precos por unidade
			Artigosprecos preco1=artigo.createPrecos("001", "UN");
			Artigosprecos preco2=artigo.createPrecos("001", "DZ");
			//precos IVA calculados automaticamente
			preco1.setPrecopvp(BigDecimal.valueOf(100));
			preco2.setPrecopvp(BigDecimal.valueOf(200));
			artigo.addPrecos(preco1);
			artigo.addPrecos(preco2);

			//grava o artigo na base de dados
			ErrorList error=ColibriDatabase.storeArtigos(artigo, false);

			//apresenta possiveis erros
			ErrorDialog.open(error.getErrors());

			return artigo;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}



	/** Mostra a coleccao de artigos */
	public void showArtigos() {

		//ARTIGOS apenas com os campos RAIZ
		Collection<Artigos> col=ColibriDatabase.getArtigosCollection();

		for(Artigos artigo: col){
			try{
				System.out.println(artigo);
				//carrega o artigo completo
				Artigos artigo2=ColibriDatabase.loadArtigos(artigo.getCodigo());
				//saca a primeira unidade medida
				System.out.println(artigo2.getUnidades(0).getUnidademedida());
				//saca o primeiro preco
				System.out.println(artigo2.getPrecos(0).getPrecopvp());

			}catch(Exception e){
				e.printStackTrace();
			}

		}

	}


}
