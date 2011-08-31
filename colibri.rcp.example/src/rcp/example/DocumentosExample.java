/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.example;

import java.math.BigDecimal;

import rcp.colibri.core.business.process.EntidadesdocumentosProcess;
import rcp.colibri.core.business.process.EntidadesdocumentoslinhasProcess;
import rcp.colibri.core.business.rules.EntidadesdocumentoslinhasRules;
import rcp.colibri.core.vars.model.TableVARS;
import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.model.classes.Documentosseries;
import rcp.colibri.dao.model.classes.Entidades;
import rcp.colibri.dao.model.classes.Entidadesdocumentos;
import rcp.colibri.dao.model.classes.Entidadesdocumentoslinhas;

public class DocumentosExample {

	public DocumentosExample(){
	}


	/**
	 * Grava um novo documento
	 */
	public boolean storeDocumento(Entidadesdocumentos documento, boolean edited) {
		try{

			//grava o documento na base de dados
			return !ColibriDatabase.storeEntidadesdocumentos(documento,
				documento.getLinhasdocumento(),
				documento.getTabelaiva(),
				documento.getEntregaspagamento(),
				edited).hasErrors();

		} catch (Exception e){
			e.printStackTrace();
		}

		return false;

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

			storeDocumento(documento, false);

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
			linha.setArtigo(ColibriDatabase.loadArtigos("1"));

			//inicializa elementos dependentes da entidade
			EntidadesdocumentoslinhasRules.linha(linha, entidade);

			//actualiza a quantidade
			linha.setQuantidade(BigDecimal.valueOf(5));
			EntidadesdocumentoslinhasProcess.process(documento, linha, TableVARS.entidadesdocumentoslinhas_quantidade);

			//actualiza o preco
			linha.setPreco(BigDecimal.valueOf(100));
			EntidadesdocumentoslinhasProcess.process(documento, linha, TableVARS.entidadesdocumentoslinhas_preco);

			//adiciona a linha ao documento
			documento.addLinhasdocumento(linha);

		} catch (Exception e){
			e.printStackTrace();
		}

	}


}
