/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example;

import java.math.BigDecimal;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.dma.java.utils.array.ErrorList;
import org.dma.java.utils.numeric.NumericUtils;
import org.dma.java.utils.string.StringUtils;

import org.projectocolibri.rcp.colibri.core.vars.DatabaseVARS.FIELDS;
import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.model.classes.Codigospostais;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidadesdocumentos;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidadesdocumentoslinhas;

public class DocumentosExample {

	public DocumentosExample() {}


	/** Cria um novo documento */
	public Entidadesdocumentos createDocumento(String tipodocumento, String entidade, String artigo) {
		try{
			//cria objecto documento
			Entidadesdocumentos documento=new Entidadesdocumentos(ColibriDatabase.loadDocumentostipos(tipodocumento));

			//insere entidade no documento
			documento.setEntidade(ColibriDatabase.loadEntidades(entidade));

			//FACULTATIVO: actualiza a morada
			documento.setMorada("Rua da Liberdade "+StringUtils.randomNumbers(3));
			documento.setCodigopostal(createCodigopostal());

			//adiciona linhas ao documento
			documento.addLinhasdocumento(createLinhasdocumento(documento, artigo));

			//processa regras do documento
			documento.process();

			//grava o documento na base de dados
			ErrorList error=ColibriDatabase.storeEntidadesdocumentos(documento, false);

			//apresenta possiveis erros
			ErrorDialog.open(error.getErrors());

			return documento;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


	/** Cria um novo codigo postal */
	public Codigospostais createCodigopostal() {
		try{
			//cria um codigo aleatorio
			String codigo=NumericUtils.random(4)+"-"+NumericUtils.random(3);

			//carrega o codigo postal
			Codigospostais codigopostal=ColibriDatabase.loadCodigospostais(codigo);

			//codigo postal nao existe?
			return codigopostal==null ?
				//inicializa um novo codigo postal
				new Codigospostais(codigo, StringUtils.randomLetters(FIELDS.codigospostais_descricao.size.size)) :
				codigopostal;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


	/** Cria uma linha do documento */
	public Entidadesdocumentoslinhas createLinhasdocumento(Entidadesdocumentos documento, String artigo) {
		try{
			//cria objecto linha
			Entidadesdocumentoslinhas linha=documento.createLinhasdocumento();

			//insere o artigo na linha
			linha.setArtigo(ColibriDatabase.loadArtigos(artigo,false));

			//inicializa a quantidade
			linha.setQuantidade(BigDecimal.valueOf(NumericUtils.random(2)));

			//inicializa o preco
			linha.setPreco(BigDecimal.valueOf(NumericUtils.random(4)));
			//ALTERNATIVO: preco automatico
			linha.setPreco$Entidade();

			//FACULTATIVO: inicializa o valor desconto
			linha.setValordesconto(BigDecimal.valueOf(100));

			//processa a linha
			linha.process();

			return linha;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


}
