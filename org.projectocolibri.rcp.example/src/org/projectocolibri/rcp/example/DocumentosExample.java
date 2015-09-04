/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example;

import java.math.BigDecimal;

import org.dma.java.math.NumericUtils;
import org.dma.java.util.StringUtils;

import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.mapper.TableMap.FIELDS;
import org.projectocolibri.rcp.colibri.dao.database.model.Codigospostais;
import org.projectocolibri.rcp.colibri.dao.database.model.Entidadesdocumentos;
import org.projectocolibri.rcp.colibri.dao.database.model.Entidadesdocumentoslinhas;

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
			String codigo=StringUtils.randomNumbers(4)+"-"+StringUtils.randomNumbers(3);

			//carrega o codigo postal
			Codigospostais codigopostal=ColibriDatabase.loadCodigospostais(codigo);

			//codigo postal nao existe?
			return codigopostal==null ?
				//inicializa um novo codigo postal
				new Codigospostais(codigo, StringUtils.randomLetters(
						FIELDS.codigospostais_descricao.size.size)) : codigopostal;

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

			//inicializa a quantidade
			linha.setQuantidade(BigDecimal.valueOf(NumericUtils.random(2)));

			//insere o artigo na linha
			linha.setArtigo(ColibriDatabase.loadArtigos(artigo, false));

			//artigo NAO existe?
			if (linha.getArtigo()==null){
				//descricao manual
				linha.setDescricao("ARTIGO GENERICO");
				//preco manual
				linha.setPreco(BigDecimal.valueOf(NumericUtils.random(4)));
			}else{
				//preco automatico
				linha.setPreco$Entidade();
			}

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
