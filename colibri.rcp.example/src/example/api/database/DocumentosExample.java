/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package example.api.database;

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

public class DocumentosExample {

	public DocumentosExample(){
	}


	/**
	 * Grava um novo documento
	 */
	public boolean storeDocumento() {
		try{

			Entidadesdocumentos documento=createDocumento();

			/*
			//altera a serie do documento
			documento.setSeriedocumento(DatabaseManager.loadDocumentosseries(
					Documentosseries.generateKey(documento.getTipodocumento().getCodigo(), "0000")));
			*/

			//grava o documento na base de dados
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

			//cria objecto documento
			Entidadesdocumentos documento=new Entidadesdocumentos(DatabaseManager.loadDocumentostipos("CFA"));

			//carrega uma entidade do tipo CLIENTE
			Entidades entidade=DatabaseManager.loadEntidades(Entidades.generateKey("CL", 1));
			//inicializa a entidade do documento
			documento.setEntidade(entidade);

			//cria as linhas do documento
			createLinhasdocumento(documento, entidade);

			//processa totais e tabela de iva
			EntidadesdocumentosProcess.process(documento, documento.getLinhasdocumento());
			//efectua processamentos adicionais
			EntidadesdocumentosProcess.postProcess(documento, documento.getLinhasdocumento(), false);

			return documento;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


	/**
	 * Cria as linhas do documento
	 */
	public void createLinhasdocumento(Entidadesdocumentos documento, Entidades entidade) {
		try{

			//cria uma nova linha
			Entidadesdocumentoslinhas linha=createLinha(documento, entidade, DatabaseManager.loadArtigos("1"));

			//actualiza a quantidade
			linha.setQuantidade(BigDecimal.valueOf(5));
			EntidadesdocumentoslinhasProcess.process(documento, linha, TableVARS.entidadesdocumentoslinhas_quantidade);

			//actualiza o preco
			linha.setPreco(BigDecimal.valueOf(100));
			EntidadesdocumentoslinhasProcess.process(documento, linha, TableVARS.entidadesdocumentoslinhas_preco);

			//adiciona a linha ao documento
			documento.addLinhasdocumento(linha);

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	/**
	 * Cria uma nova linha
	 */
	public Entidadesdocumentoslinhas createLinha(Entidadesdocumentos documento, Entidades entidade, Artigos artigo) {
		try{

			//cria objecto linha
			Entidadesdocumentoslinhas linha=new Entidadesdocumentoslinhas(entidade, entidade.getDescontofactura());

			//inicializa o numero da linha (comeca em 1)
			linha.setNumerolinha(documento.getLinhasdocumento().size()+1);

			//inicializa elementos dependentes da entidade
			EntidadesdocumentoslinhasRules.linha(linha, artigo, entidade);

			return linha;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


}
