/*******************************************************************************
 * 2008-2014 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.importador;

import java.io.File;
import java.math.BigDecimal;

import org.dma.java.utils.Debug;
import org.dma.java.utils.string.StringUtils;

import org.projectocolibri.rcp.colibri.core.support.RegexSupport;
import org.projectocolibri.rcp.colibri.core.vars.ComboVARS;
import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.fisco.saft.ISAFTImport;
import org.projectocolibri.rcp.colibri.dao.model.classes.Artigos;
import org.projectocolibri.rcp.colibri.dao.model.classes.Codigosiva;
import org.projectocolibri.rcp.colibri.dao.model.classes.Codigospostais;
import org.projectocolibri.rcp.colibri.dao.model.classes.Empresa;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidades;
import org.projectocolibri.rcp.colibri.dao.model.classes.Entidadestipos;
import org.projectocolibri.rcp.colibri.dao.model.classes.Familias;
import org.projectocolibri.rcp.colibri.dao.model.classes.Paises;

import x0101.oecdStandardAuditFileTaxPT1.AddressStructure;
import x0101.oecdStandardAuditFileTaxPT1.AddressStructurePT;
import x0101.oecdStandardAuditFileTaxPT1.AuditFileDocument;
import x0101.oecdStandardAuditFileTaxPT1.AuditFileDocument.AuditFile;
import x0101.oecdStandardAuditFileTaxPT1.AuditFileDocument.AuditFile.MasterFiles;
import x0101.oecdStandardAuditFileTaxPT1.CustomerDocument.Customer;
import x0101.oecdStandardAuditFileTaxPT1.HeaderDocument.Header;
import x0101.oecdStandardAuditFileTaxPT1.ProductDocument.Product;
import x0101.oecdStandardAuditFileTaxPT1.TaxTableDocument.TaxTable;
import x0101.oecdStandardAuditFileTaxPT1.TaxTableEntryDocument.TaxTableEntry;

public abstract class SAFTx0101Import implements ISAFTImport {

	public abstract void consoleOut(String string);

	//Parametros
	private final boolean importHeader;
	private final boolean importCustomers;
	private final boolean importArticles;
	private final boolean importTaxes;
	private final Entidadestipos tipoentidade;

	/*
	 * Documento principal
	 */
	private AuditFile auditFile;

	/*
	 * Grupos obrigatorios do Audit File
	 *
	 * 	- Header
	 *
	 * 	- Master Files
	 * 		-> Customer
	 * 		-> Tax Table
	 * 		-> Product
	 *
	 * 	- Source Documents
	 * 		-> Sales Invoices
	 */
	public SAFTx0101Import(boolean importHeader, boolean importCustomers,
			boolean importArticles, boolean importTaxes, String tipoentidade) {

		this.importHeader=importHeader;
		this.importCustomers=importCustomers;
		this.importArticles=importArticles;
		this.importTaxes=importTaxes;
		this.tipoentidade=ColibriDatabase.loadEntidadestipos(tipoentidade);

	}


	/*
	 * (non-Javadoc)
	 * @see org.projectocolibri.rcp.colibri.dao.saft.ISAFTImport#process()
	 */
	public void process() throws Exception {

		Header header=auditFile.getHeader();
		MasterFiles masterFiles=auditFile.getMasterFiles();

		if(importHeader) populateEmpresa(header);
		if(importCustomers) populateClientes(masterFiles);
		if(importArticles) populateArtigos(masterFiles);
		if(importTaxes) populateIVA(masterFiles);

		Debug.out("VALID", isValid());

	}


	public boolean loadFile(File file) {

		try{
			AuditFileDocument auditFileDocument=AuditFileDocument.Factory.parse(file);
			auditFile=auditFileDocument.getAuditFile();

			Debug.out("VALID", isFileValid());

			return isFileValid();

		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}

	public boolean isFileValid() {
		return auditFile!=null;
	}

	public boolean isValid() {
		return isFileValid() && auditFile.validate();
	}


	private void populateEmpresa(Header header) throws Exception {

		Empresa empresa=ColibriDatabase.getEmpresa();

		//conservatoria e registo comercial
		if(!header.getCompanyID().isEmpty() && header.getCompanyID().contains(" ")){
			String[] companyId=header.getCompanyID().split(" ");
			empresa.setConservatoria(parse(companyId[0]));
			empresa.setRegistocomercial(parse(companyId[1]));
		}

		empresa.setNif(String.valueOf(header.getTaxRegistrationNumber()));
		empresa.setNome(parse(header.getCompanyName()));

		AddressStructurePT companyAddress=header.getCompanyAddress();
		empresa.setMorada(parse(companyAddress.getAddressDetail()));
		empresa.setLocalidade(parse(companyAddress.getCity()));
		empresa.setCodigopostal(storeCodigospostais(companyAddress.getPostalCode(), companyAddress.getCity()));

		/*
		empresa.setPais(storePaises(
			((Country)companyAddress.getCountry()).getStringValue(),
			((Country)companyAddress.getCountry()).getStringValue()));
		*/

		empresa.setTelefone(parse(header.getTelephone()));
		empresa.setFax(parse(header.getFax()));
		empresa.setEmail(parse(header.getEmail()));
		empresa.setUrl(parse(header.getWebsite()));

		Debug.out(empresa);
		ColibriDatabase.storeEmpresa(empresa);

		consoleOut("Empresa: " + empresa.getNome() + " criada.");

	}


	private void populateClientes(MasterFiles masterFiles) throws Exception {

		Integer numero=ColibriDatabase.getEntidades$NextNumero(tipoentidade.getCodigo());

		for (Customer customer: masterFiles.getCustomerArray()) {

			Entidades entidade=new Entidades(tipoentidade,numero);
			numero++;

			entidade.setNif(parse(customer.getCustomerTaxID()));
			entidade.setNome(parse(customer.getCompanyName()));
			entidade.setResponsavel(parse(customer.getContact()));

			AddressStructure addressStructure=customer.getBillingAddress();
			entidade.setMorada(parse(addressStructure.getAddressDetail()));
			entidade.setCodigopostal(storeCodigospostais(addressStructure.getPostalCode(), addressStructure.getCity()));
			entidade.setLocalidade(parse(addressStructure.getCity()));
			entidade.setPais(storePaises(addressStructure.getCountry(), addressStructure.getCountry()));

			entidade.setTelefone(parse(customer.getTelephone()));
			entidade.setFax(parse(customer.getFax()));
			entidade.setEmail(parse(customer.getEmail()));
			entidade.setUrl(parse(customer.getWebsite()));

			Debug.out(entidade);
			ColibriDatabase.storeEntidades(entidade);
			consoleOut("Cliente: " + entidade.getNome() + " criado.");
		}

	}


	private void populateArtigos(MasterFiles masterFiles) throws Exception {

		for (Product product: masterFiles.getProductArray()) {

			if(!ColibriDatabase.existsArtigos(product.getProductCode())){

				Artigos artigo=new Artigos(
						parse(product.getProductCode()),
						parse(product.getProductDescription()));
				artigo.addUnidades();
				artigo.addPrecos();

				if(product.getProductType().equals("P")){
					artigo.setTipo(ComboVARS.artigos_tipo_produto);
				}else if(product.getProductType().equals("S")){
					artigo.setTipo(ComboVARS.artigos_tipo_servico);
				}else if(product.getProductType().equals("I")){
					artigo.setTipo(ComboVARS.artigos_tipo_imposto);
				}

				artigo.setFamilia(storeFamilias(product.getProductGroup(), product.getProductGroup()));

				//inicializa codigo de barras
				if(!product.getProductNumberCode().equals(product.getProductCode())){
					artigo.getUnidades(0).setCodigobarras(parse(product.getProductNumberCode()));
				}

				Debug.out(artigo);
				ColibriDatabase.storeArtigos(artigo);
				consoleOut("Artigo: " + artigo.getDescricao() + " criado.");
			}

		}

	}


	private void populateIVA(MasterFiles masterFiles) throws Exception {

		for (TaxTable taxTable: masterFiles.getTaxTableArray()) {

			for (TaxTableEntry taxTableEntry: taxTable.getTaxTableEntryArray()) {

				storeCodigosiva(
					parse(taxTableEntry.getTaxCode()),
					parse(taxTableEntry.getDescription()),
					taxTableEntry.getTaxPercentage(),
					getTipotaxa(taxTableEntry.getTaxCode()),
					getEspacoFiscal(taxTableEntry.getTaxCountryRegion()));
			}

		}

	}


	private int getTipotaxa(String codigo) throws Exception {

		int tipotaxa=ComboVARS.codigosiva_tipotaxa_outras;

		if(codigo.equals("Int") || codigo.equals("1")){
			tipotaxa=ComboVARS.codigosiva_tipotaxa_intermedia;
		}else if(codigo.equals("Nor") || codigo.equals("2")){
			tipotaxa=ComboVARS.codigosiva_tipotaxa_normal;
		}else if(codigo.equals("Red") || codigo.equals("3")){
			tipotaxa=ComboVARS.codigosiva_tipotaxa_reduzida;
		}else if(codigo.equals("Ise") || codigo.equals("4")){
			tipotaxa=ComboVARS.codigosiva_tipotaxa_isenta;
		}

		return tipotaxa;

	}


	private int getEspacoFiscal(String codigo) throws Exception {

		int espacofiscal=ComboVARS.codigosiva_espacofiscal_continente;

		if(codigo.equals("IVARAA")){
			espacofiscal=ComboVARS.codigosiva_espacofiscal_acores;
		}else if(codigo.equals("IVARAM")){
			espacofiscal=ComboVARS.codigosiva_espacofiscal_madeira;
		}

		return espacofiscal;

	}





	/*
	 * Base de Dados
	 */
	private Codigospostais storeCodigospostais(String codigo, String descricao) throws Exception {

		if (!ColibriDatabase.existsCodigospostais(codigo)){
			Codigospostais codigopostal=new Codigospostais(parse(codigo),parse(descricao));
			Debug.out(codigopostal);
			ColibriDatabase.storeCodigospostais(codigopostal);
		}

		return ColibriDatabase.loadCodigospostais(codigo);

	}


	private Paises storePaises(String codigo, String descricao) throws Exception {

		if (!ColibriDatabase.existsPaises(codigo)){
			Paises pais=new Paises(parse(codigo),parse(descricao));
			Debug.out(pais);
			ColibriDatabase.storePaises(pais);
		}

		return ColibriDatabase.loadPaises(codigo);

	}


	private Familias storeFamilias(String codigo, String descricao) throws Exception {

		if (!ColibriDatabase.existsFamilias(codigo)){
			Familias familia=new Familias(parse(codigo),parse(descricao));
			Debug.out(familia);
			ColibriDatabase.storeFamilias(familia);
		}

		return ColibriDatabase.loadFamilias(codigo);

	}


	private Codigosiva storeCodigosiva(String codigo, String descricao, BigDecimal taxa, int tipotaxa, int espacofiscal) throws Exception {

		if (!ColibriDatabase.existsCodigosiva(codigo)){
			Codigosiva codigoiva=new Codigosiva(parse(codigo),parse(descricao),tipotaxa,espacofiscal,taxa);
			Debug.out(codigoiva);
			ColibriDatabase.storeCodigosiva(codigoiva);
		}

		return ColibriDatabase.loadCodigosiva(codigo);

	}



	/** Evita strings a null e remove caracteres nao permitidos */
	private String parse(String string) throws Exception {
		return string==null ? "" : StringUtils.removeChars(string, RegexSupport.EXCLUDE_CHARS);
	}


}