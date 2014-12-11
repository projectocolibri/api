/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.example;

import java.util.Collection;

import org.dma.eclipse.swt.dialogs.message.ErrorDialog;
import org.dma.java.utils.array.ErrorList;
import org.dma.java.utils.numeric.NumericUtils;
import org.dma.java.utils.string.StringUtils;

import org.projectocolibri.rcp.colibri.core.vars.DatabaseVARS.FIELDS;
import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.model.classes.Codigospostais;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidades;

public class EntidadesExample {

	public EntidadesExample() {}


	/** Cria uma nova entidade */
	public Entidades createCliente(String tipoentidade, String nome) {
		try{
			//cria objecto entidade
			Entidades entidade=new Entidades(ColibriDatabase.loadEntidadestipos(tipoentidade));

			entidade.setNome(nome);

			//FACULTATIVO: actualiza o codigo postal
			entidade.setCodigopostal(createCodigopostal());

			//grava a entidade na base de dados
			ErrorList error=ColibriDatabase.storeEntidades(entidade,false);

			//apresenta possiveis erros
			ErrorDialog.open(error.getErrors());

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
			String codigo=NumericUtils.random(4)+"-"+NumericUtils.random(3);

			//carrega o codigo postal
			Codigospostais codigopostal=ColibriDatabase.loadCodigospostais(codigo);

			//codigo postal nao existe?
			return codigopostal==null ?
				//inicializa um novo codigo postal
				new Codigospostais(codigo, StringUtils.randomLetters(FIELDS.codigospostais_descricao.size.size)) :
				codigopostal;

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


	/** Mostra a coleccao de entidades */
	public void showEntidades(String tipoentidade) {

		//ENTIDADES apenas com os campos RAIZ
		Collection<Entidades> col=ColibriDatabase.getEntidadesCollection(tipoentidade);

		for(Entidades entidade: col){
			try{
				System.out.println(entidade);

			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}


}
