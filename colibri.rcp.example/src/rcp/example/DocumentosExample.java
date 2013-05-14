/*******************************************************************************
 * 2008-2012 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import java.math.BigDecimal;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.dma.java.utils.array.ErrorList;

import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.model.classes.Entidades;
import rcp.colibri.dao.model.classes.Entidadesdocumentos;
import rcp.colibri.dao.model.classes.Entidadesdocumentoslinhas;
import rcp.colibri.dao.model.process.EntidadesdocumentosProcess;
import rcp.colibri.dao.model.rules.EntidadesdocumentosRules;

public class DocumentosExample {

	public DocumentosExample(){
	}


	/**
	 * Cria um novo documento
	 */
	public void createDocumento(String tipoentidade) {
		try{
			//cria objecto documento
			Entidadesdocumentos documento=new Entidadesdocumentos(ColibriDatabase.loadDocumentostipos(tipoentidade));

			//carrega a entidade
			Entidades entidade=ColibriDatabase.loadEntidades(
				Entidades.generateKey(documento.getTipoentidade().getCodigo(),1));

			//insere entidade no documento
			documento.setEntidade(entidade);

			//inicializa o documento
			EntidadesdocumentosRules.init(documento, entidade);

			//cria as linhas do documento
			createLinhasdocumento(documento, entidade);

			//processa regras do documento
			EntidadesdocumentosProcess.rules(documento, documento.getLinhasdocumento());

			//grava o documento na base de dados
			ErrorList error=ColibriDatabase.storeEntidadesdocumentos(documento, false);

			//apresenta possiveis erros
			ErrorDialog.open(error.getErrors());

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

			//inicializa a linha
			EntidadesdocumentosRules.Linhas.init(linha, entidade, ColibriDatabase.loadArtigos("1"));

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
