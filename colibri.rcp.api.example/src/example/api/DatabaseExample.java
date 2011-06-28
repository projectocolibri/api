/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example.api;

import java.math.BigDecimal;

import rcpcolibri.business.process.EntidadesdocumentosProcess;
import rcpcolibri.business.process.EntidadesdocumentoslinhasProcess;
import rcpcolibri.business.rules.EntidadesdocumentoslinhasRules;
import rcpcolibri.dao.database.DatabaseManager;
import rcpcolibri.dao.model.classes.Artigos;
import rcpcolibri.dao.model.classes.Entidades;
import rcpcolibri.dao.model.classes.Entidadesdocumentos;
import rcpcolibri.dao.model.classes.Entidadesdocumentoslinhas;
import rcpcolibri.vars.model.TableVARS;

public class DatabaseExample {

	public DatabaseExample(){
	}


	/**
	 * Grava um novo documento
	 */
	public boolean storeDocumento() {
		try{

			Entidadesdocumentos documento=createDocumento();
			/*
			documento.setSeriedocumento(DatabaseManager.loadDocumentosseries(
					Documentosseries.generateKey(documento.getTipodocumento().getCodigo(), "0000")));
			*/

			return !DatabaseManager.storeEntidadesdocumentos(documento,
				documento.getLinhasdocumento(),
				documento.getTabelaiva(),
				documento.getEntregaspagamento(),
				false).hasErrors();

		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}


	/**
	 * Cria um novo documento
	 */
	public Entidadesdocumentos createDocumento() {
		try{

			Entidadesdocumentos documento=new Entidadesdocumentos(DatabaseManager.loadDocumentostipos("CFA"));
			Entidades entidade=DatabaseManager.loadEntidades(Entidades.generateKey("CL", 1));
			documento.setEntidade(entidade);

			Entidadesdocumentoslinhas linha=createLinha(1, entidade, DatabaseManager.loadArtigos("1"));
			linha.setQuantidade(BigDecimal.valueOf(5));
			EntidadesdocumentoslinhasProcess.process(documento, linha, TableVARS.entidadesdocumentoslinhas_quantidade);
			linha.setPreco(BigDecimal.valueOf(100));
			EntidadesdocumentoslinhasProcess.process(documento, linha, TableVARS.entidadesdocumentoslinhas_preco);
			documento.addLinhasdocumento(linha);

			EntidadesdocumentosProcess.process(documento, documento.getLinhasdocumento());
			EntidadesdocumentosProcess.postProcess(documento, documento.getLinhasdocumento(), false);

			return documento;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


	/**
	 * Cria uma nova linha
	 */
	public Entidadesdocumentoslinhas createLinha(int numerolinha, Entidades entidade, Artigos artigo) {
		try{

			Entidadesdocumentoslinhas linha=new Entidadesdocumentoslinhas(entidade, entidade.getDescontofactura());
			linha.setNumerolinha(numerolinha);

			EntidadesdocumentoslinhasRules.linha(linha, artigo, entidade);

			return linha;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


}
