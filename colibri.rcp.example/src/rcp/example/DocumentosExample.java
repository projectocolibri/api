/*******************************************************************************
 * 2008-2012 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import java.math.BigDecimal;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.dma.java.utils.array.ErrorList;
import org.dma.java.utils.string.StringUtils;

import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.model.classes.Codigospostais;
import rcp.colibri.dao.model.classes.Entidadesdocumentos;
import rcp.colibri.dao.model.classes.Entidadesdocumentoslinhas;
import rcp.colibri.dao.model.process.EntidadesdocumentosProcess;
import rcp.colibri.dao.model.rules.EntidadesdocumentosRules;
import rcp.colibri.vars.database.DatabaseVARS.FIELDS;

public class DocumentosExample {

	public DocumentosExample(){
	}


	/**
	 * Cria um novo documento
	 */
	public Entidadesdocumentos createDocumento(String tipodocumento, String entidade, String artigo) {
		try{
			//cria objecto documento
			Entidadesdocumentos documento=new Entidadesdocumentos(
					ColibriDatabase.loadDocumentostipos(tipodocumento));

			//insere entidade no documento
			documento.setEntidade(ColibriDatabase.loadEntidades(entidade));

			//inicializa o documento
			EntidadesdocumentosRules.entidade(documento, documento.getEntidade());
			
			//inicializa codigo postal
			createCodigopostal(documento);

			//cria as linhas do documento
			createLinhasdocumento(documento, artigo);

			//processa regras do documento
			EntidadesdocumentosProcess.rules(documento, documento.getLinhasdocumento());

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
	private void createCodigopostal(Entidadesdocumentos documento) {
		try{
			String codigo=StringUtils.random(4)+"-"+StringUtils.random(3);
			
			Codigospostais codigopostal=ColibriDatabase.loadCodigospostais(codigo);
			
			if(codigopostal==null){
				codigopostal=new Codigospostais(codigo, 
						StringUtils.random(FIELDS.codigospostais_descricao.size.size), "");
			}
			
			documento.setCodigopostal(codigopostal);
			documento.setLocalidade(codigopostal.getDescricao());

		} catch (Exception e){
			e.printStackTrace();
		}

	}


	/**
	 * Cria as linhas do documento
	 */
	private void createLinhasdocumento(Entidadesdocumentos documento, String artigo) {
		try{
			//cria objecto linha
			Entidadesdocumentoslinhas linha=documento.createLinhasdocumento();

			//inicializa a linha
			EntidadesdocumentosRules.Facturas.Linhas.artigo(linha, 
					documento.getEntidade(), ColibriDatabase.loadArtigos(artigo));

			//actualiza a quantidade
			linha.setQuantidade(BigDecimal.valueOf(5));

			//actualiza o preco
			linha.setPreco(BigDecimal.valueOf(100));

			//processa a linha
			EntidadesdocumentosProcess.Linhas.rules(documento, linha);

			//adiciona a linha ao documento
			documento.addLinhasdocumento(linha);

		} catch (Exception e){
			e.printStackTrace();
		}

	}


}
