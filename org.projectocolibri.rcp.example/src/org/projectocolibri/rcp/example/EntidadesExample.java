/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example;

import java.util.Collection;

import org.dma.java.util.StringUtils;

import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.mapper.TableMap.FIELDS;
import org.projectocolibri.rcp.colibri.dao.database.model.Codigospostais;
import org.projectocolibri.rcp.colibri.dao.database.model.Entidades;

public class EntidadesExample {

	public EntidadesExample() {}


	/** Cria uma nova entidade */
	public Entidades createCliente(String tipoentidade, String nome) {
		try{
			//cria objecto entidade
			Entidades entidade=new Entidades(tipoentidade);

			entidade.setNome(nome);

			//FACULTATIVO: actualiza o codigo postal
			entidade.setCodigopostal(createCodigopostal());

			return entidade;

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


	/** Mostra a coleccao de entidades */
	public void showEntidades(String tipoentidade) {

		//ENTIDADES apenas com os campos RAIZ
		Collection<Entidades> col=ColibriDatabase.getEntidadesCollection(tipoentidade);

		for(Entidades e: col) try{

			System.out.println(e);

		}catch(Exception e1){
			e1.printStackTrace();
		}

	}


}
