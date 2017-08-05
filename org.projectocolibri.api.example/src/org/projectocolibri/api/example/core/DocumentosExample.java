/*******************************************************************************
 * 2008-2017 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.api.example.core;

import java.math.BigDecimal;
import java.util.Collection;

import org.dma.java.math.NumericUtils;
import org.dma.java.util.CollectionUtils;
import org.dma.java.util.StringUtils;

import org.projectocolibri.api.database.ColibriDatabase;
import org.projectocolibri.api.database.mapper.TableMap.FIELDS;
import org.projectocolibri.api.database.model.Artigos;
import org.projectocolibri.api.database.model.Codigospostais;
import org.projectocolibri.api.database.model.Documentostipos;
import org.projectocolibri.api.database.model.Entidades;
import org.projectocolibri.api.database.model.Entidadesdocumentos;
import org.projectocolibri.api.database.model.Entidadesdocumentoslinhas;
import org.projectocolibri.api.database.model.Entidadestipos;

public class DocumentosExample {

	public void run() {

		try{
			storeDocumento();

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	/** Grava um novo documento */
	public Entidadesdocumentos storeDocumento() {

		//cria um novo documento
		Entidadesdocumentos documento=createDocumento(Documentostipos.RECORDS.clientes_factura.codigo);

		//grava e apresenta possiveis erros
		ColibriDatabase.storeEntidadesdocumentos(documento).print();

		return documento;

	}


	/** Cria um novo documento */
	public Entidadesdocumentos createDocumento(String tipodocumento) {

		new EntidadesExample().storeEntidade();
		//carrega clientes existentes
		Collection<Entidades> entidades=ColibriDatabase.getEntidadesCollection(Entidadestipos.RECORDS.cliente.codigo);

		new ArtigosExample().storeArtigo();
		//carrega artigos existentes
		Collection<Artigos> artigos=ColibriDatabase.getArtigosCollection();

		return createDocumento(tipodocumento,
				CollectionUtils.random(entidades).getKey(),
				CollectionUtils.random(artigos).getCodigo(),
				CollectionUtils.random(artigos).getCodigo(),
				CollectionUtils.random(artigos).getCodigo());

	}


	/** Cria um novo documento */
	public Entidadesdocumentos createDocumento(String tipodocumento, String entidade, String...artigo) {

		//cria objecto documento
		Entidadesdocumentos documento=ColibriDatabase.loadEntidades(entidade).
				createDocumento(tipodocumento);

		//FACULTATIVO: actualiza a morada
		documento.setMorada("Rua da Liberdade "+StringUtils.randomNumbers(3));
		documento.setCodigopostal(createCodigopostal());

		for(String element: artigo){
			//adiciona linhas ao documento
			documento.addLinhasdocumento(createLinhasdocumento(documento, element));
		}

		//processa documento
		documento.process();

		return documento;

	}


	/** Cria um novo codigo postal */
	private Codigospostais createCodigopostal() {

		//cria um codigo aleatorio
		String codigo=StringUtils.randomNumbers(4)+"-"+StringUtils.randomNumbers(3);

		//carrega o codigo postal
		Codigospostais codigopostal=ColibriDatabase.loadCodigospostais(codigo);

		//codigo postal nao existe?
		return codigopostal==null ?
			//inicializa um novo codigo postal
			new Codigospostais(codigo, StringUtils.randomLetters(
					FIELDS.codigospostais_descricao.size.size)) : codigopostal;

	}


	/** Cria uma linha do documento */
	private Entidadesdocumentoslinhas createLinhasdocumento(Entidadesdocumentos documento, String artigo) {

		//cria objecto linha
		Entidadesdocumentoslinhas linha=documento.createLinhasdocumento();

		//inicializa a quantidade
		linha.setQuantidade(BigDecimal.valueOf(NumericUtils.random(2)));

		//insere o artigo na linha
		linha.setArtigo(artigo);

		//NAO existe artigo?
		if (!linha.hasArtigo()){
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

		return linha;

	}


}