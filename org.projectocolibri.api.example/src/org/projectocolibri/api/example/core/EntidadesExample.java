/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.api.example.core;

import java.util.Collection;

import org.dma.java.util.StringUtils;

import org.projectocolibri.api.database.ColibriDatabase;
import org.projectocolibri.api.database.mapper.TableMap.FIELDS;
import org.projectocolibri.api.database.model.Codigospostais;
import org.projectocolibri.api.database.model.Entidades;
import org.projectocolibri.api.database.model.Entidadestipos;

public class EntidadesExample {

	public void run() {

		try{
			Entidades entidade=storeEntidade();

			showEntidades(Entidadestipos.RECORDS.cliente.codigo);

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/** Grava uma nova entidade */
	public Entidades storeEntidade() {

		//cria uma nova entidade
		Entidades entidade=createEntidade(
				Entidadestipos.RECORDS.cliente.codigo,
				StringUtils.randomLetters(FIELDS.entidades_nome.size.size/2));

		//grava e apresenta possiveis erros
		ColibriDatabase.storeEntidades(entidade).print();

		return entidade;

	}


	/** Cria uma nova entidade */
	public Entidades createEntidade(String tipoentidade, String nome) {

		//cria objecto entidade
		Entidades entidade=new Entidades(tipoentidade);

		entidade.setNome(nome);

		//FACULTATIVO: actualiza o codigo postal
		entidade.setCodigopostal(createCodigopostal());

		return entidade;

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


	/** Mostra a coleccao de entidades */
	public void showEntidades(String tipoentidade) {

		//ENTIDADES apenas com os campos RAIZ
		Collection<Entidades> col=ColibriDatabase.getEntidadesCollection(tipoentidade);

		for(Entidades element: col) try{

			//carrega ENTIDADE completa
			Entidades entidade=ColibriDatabase.loadEntidades(element.getKey());

			System.out.println(entidade);

		}catch(Exception e){
			e.printStackTrace();
		}

	}


}
