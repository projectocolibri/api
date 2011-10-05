/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import java.math.BigDecimal;

import org.dma.utils.eclipse.swt.DialogHandler;
import org.dma.utils.java.array.ErrorList;

import rcp.colibri.core.business.process.EntidadesdocumentosProcess;
import rcp.colibri.core.business.process.EntidadesdocumentoslinhasProcess;
import rcp.colibri.core.business.rules.EntidadesdocumentoslinhasRules;
import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.model.classes.Documentosseries;
import rcp.colibri.dao.model.classes.Entidades;
import rcp.colibri.dao.model.classes.Entidadesdocumentos;
import rcp.colibri.dao.model.classes.Entidadesdocumentoslinhas;

public class DocumentosExample {

	public DocumentosExample(){
	}


	/**
	 * Cria um novo documento
	 */
	public void createDocumento(String codigo, String serie) {
		try{

			//cria objecto documento
			Entidadesdocumentos documento=new Entidadesdocumentos(ColibriDatabase.loadDocumentostipos(codigo));

			if (serie!=null){
				//altera a serie do documento
				documento.setSeriedocumento(ColibriDatabase.loadDocumentosseries(
					Documentosseries.generateKey(documento.getTipodocumento().getCodigo(), serie)));
			}

			//carrega uma entidade do tipo CLIENTE
			Entidades entidade=ColibriDatabase.loadEntidades(Entidades.generateKey("CL", 1));
			//inicializa a entidade do documento
			documento.setEntidade(entidade);

			//cria as linhas do documento
			createLinhasdocumento(documento, entidade);

			//processa totais e tabela de iva
			EntidadesdocumentosProcess.process(documento, documento.getLinhasdocumento());

			//efectua processamentos adicionais
			EntidadesdocumentosProcess.postProcess(documento, documento.getLinhasdocumento(), false);

			//grava o documento na base de dados
			ErrorList error=ColibriDatabase.storeEntidadesdocumentos(documento, false);

			//apresenta possiveis erros
			DialogHandler.error(error.getErrors());

		} catch (Exception e){
			e.printStackTrace();
		}

	}


	/**
	 * Cria as linhas do documento
	 */
	public void createLinhasdocumento(Entidadesdocumentos documento, Entidades entidade) {
		try{

			//cria objecto linha
			Entidadesdocumentoslinhas linha=documento.createLinhasdocumento();

			//inicializa artigo
			EntidadesdocumentoslinhasRules.artigo(linha, entidade, ColibriDatabase.loadArtigos("1"));

			//actualiza a quantidade
			linha.setQuantidade(BigDecimal.valueOf(5));

			//actualiza o preco
			linha.setPreco(BigDecimal.valueOf(100));

			//processa a linha
			EntidadesdocumentoslinhasProcess.process(documento, linha);

			//adiciona a linha ao documento
			documento.addLinhasdocumento(linha);

		} catch (Exception e){
			e.printStackTrace();
		}

	}


}
