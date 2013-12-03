/*******************************************************************************
 * 2008-2013 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import java.math.BigDecimal;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.dma.java.utils.array.ErrorList;
import org.dma.java.utils.numeric.NumericUtils;
import org.dma.java.utils.string.StringUtils;

import rcp.colibri.core.vars.DatabaseVARS.FIELDS;
import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.model.classes.Codigospostais;
import rcp.colibri.dao.model.classes.Entidadesdocumentos;
import rcp.colibri.dao.model.classes.Entidadesdocumentoslinhas;

public class DocumentosExample {

	public DocumentosExample(){
	}


	/**
	 * Cria um novo documento
	 */
	public Entidadesdocumentos createDocumento(String tipodocumento, String entidade, String artigo) {
		try{
			//cria objecto documento
			Entidadesdocumentos documento=new Entidadesdocumentos(ColibriDatabase.loadDocumentostipos(tipodocumento));

			//insere entidade no documento
			documento.setEntidadeAndDependants(ColibriDatabase.loadEntidades(entidade));

			//FACULTATIVO: actualiza o codigo postal
			documento.setCodigopostalAndDependants(createCodigopostal());

			//adiciona linhas ao documento
			documento.addLinhasdocumento(createLinhasdocumento(documento, artigo));

			//processa regras do documento
			documento.process(documento.getLinhasdocumento());

			//grava o documento na base de dados
			ErrorList error=ColibriDatabase.storeEntidadesdocumentos(documento, false);

			//apresenta possiveis erros
			ErrorDialog.open(error.getErrors());

			return documento;

		} catch (Exception e){
			e.printStackTrace();
		}

		return null;

	}


	/**
	 * Cria um novo codigo postal
	 */
	public Codigospostais createCodigopostal() {
		try{
			//cria um codigo aleatorio
			String codigo=StringUtils.random(4)+"-"+StringUtils.random(3);

			//carrega o codigo postal
			Codigospostais codigopostal=ColibriDatabase.loadCodigospostais(codigo);

			//codigo postal nao existe?
			if(codigopostal==null){
				//inicializa um novo codigo postal
				codigopostal=new Codigospostais(codigo, StringUtils.random(FIELDS.codigospostais_descricao.size.size));
			}

			return codigopostal;

		} catch (Exception e){
			e.printStackTrace();
		}

		return null;

	}


	/**
	 * Cria uma linha do documento
	 */
	public Entidadesdocumentoslinhas createLinhasdocumento(Entidadesdocumentos documento, String artigo) {
		try{
			//cria objecto linha
			Entidadesdocumentoslinhas linha=documento.createLinhasdocumento();

			//insere o artigo na linha
			linha.setArtigoAndDependants(ColibriDatabase.loadArtigos(artigo));

			//FACULTATIVO: actualiza a quantidade
			linha.setQuantidade(BigDecimal.valueOf(NumericUtils.random(2)));

			//FACULTATIVO: actualiza o preco
			linha.setPreco(BigDecimal.valueOf(NumericUtils.random(4)));

			//processa a linha
			linha.process();

			return linha;

		} catch (Exception e){
			e.printStackTrace();
		}

		return null;

	}


}
