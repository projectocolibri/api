/*******************************************************************************
 * 2008-2015 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.importador;

import java.io.File;
import java.math.BigDecimal;

import org.dma.java.util.Debug;
import org.dma.java.util.StringUtils;

import org.projectocolibri.rcp.colibri.core.support.RegexSupport;
import org.projectocolibri.rcp.colibri.core.vars.ComboVARS;
import org.projectocolibri.rcp.colibri.dao.database.ColibriDatabase;
import org.projectocolibri.rcp.colibri.dao.database.model.Artigos;
import org.projectocolibri.rcp.colibri.dao.database.model.Codigosiva;
import org.projectocolibri.rcp.colibri.dao.database.model.Codigospostais;
import org.projectocolibri.rcp.colibri.dao.database.model.Empresa;
import org.projectocolibri.rcp.colibri.dao.database.model.Entidades;
import org.projectocolibri.rcp.colibri.dao.database.model.Entidadestipos;
import org.projectocolibri.rcp.colibri.dao.database.model.Familias;
import org.projectocolibri.rcp.colibri.dao.database.model.Paises;
import org.projectocolibri.rcp.colibri.dao.xml.beans.saft.ISAFTImport;

import x0101.oecdStandardAuditFileTaxPT1.AddressStructure;
import x0101.oecdStandardAuditFileTaxPT1.AddressStructurePT;
import x0101.oecdStandardAuditFileTaxPT1.AuditFileDocument;
import x0101.oecdStandardAuditFileTaxPT1.AuditFileDocument.AuditFile;
import x0101.oecdStandardAuditFileTaxPT1.AuditFileDocument.AuditFile.MasterFiles;
import x0101.oecdStandardAuditFileTaxPT1.CustomerDocument.Customer;
import x0101.oecdStandardAuditFileTaxPT1.HeaderDocument.Header;
import x0101.oecdStandardAuditFileTaxPT1.ProductDocument.Product;
import x0101.oecdStandardAuditFileTaxPT1.ProductTypeDocument.ProductType;
import x0101.oecdStandardAuditFileTaxPT1.TaxTableDocument.TaxTable;
import x0101.oecdStandardAuditFileTaxPT1.TaxTableEntryDocument.TaxTableEntry;

public abstract class SAFTx0101Import implements ISAFTImport {

	public abstract void consoleOut(String string);

	//Parametros
	private final boolean importHeader;
	private final boolean importTaxes;
	private final boolean importArticles;
	private final boolean importCustomers;
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
	 * 		-> Taxes
	 * 		-> Products
	 * 		-> Customers
	 *
	 * 	- Source Documents
	 * 		-> Sales Invoices
	 */
	public SAFTx0101Import(boolean importHeader, boolean importTaxes, boolean importArticles,
			boolean importCustomers, String tipoentidade) {

		this.importHeader=importHeader;
		this.importTaxes=importTaxes;
		this.importArticles=importArticles;
		this.importCustomers=importCustomers;
		this.tipoentidade=ColibriDatabase.loadEntidadestipos(tipoentidade);

	}


	/*
	 * (non-Javadoc)
	 * @see org.projectocolibri.rcp.colibri.dao.saft.ISAFTImport#process()
	 */
	@Override
	public void process() throws Exception {

		Header header=auditFile.getHeader();
		MasterFiles masterFiles=auditFile.getMasterFiles();

		if(importHeader) populateEmpresa(header);
		if(importTaxes) populateIVA(masterFiles);
		if(importArticles) populateArtigos(masterFiles);
		if(importCustomers) populateClientes(masterFiles);

		Debug.out("VALID", isValid());

	}

	@Override
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

	@Override
	public boolean isFileValid() {
		return auditFile!=null;
	}

	@Override
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


	private void populateIVA(MasterFiles masterFiles) throws Exception {

		for(TaxTable taxTable: masterFiles.getTaxTableList()) {

			for(TaxTableEntry taxTableEntry: taxTable.getTaxTableEntryList()) {

				storeCodigosiva(
					parse(taxTableEntry.getTaxCode()),
					parse(taxTableEntry.getDescription()),
					taxTableEntry.getTaxPercentage(),
					getTipotaxa(taxTableEntry.getTaxCode()),
					getEspacofiscal(taxTableEntry.getTaxCountryRegion()));
			}

		}

	}


	private int getTipotaxa(String codigo) throws Exception {

		if(codigo.equals("NOR")){
			return ComboVARS.codigosiva_tipotaxa_normal;
		}else if(codigo.equals("RED")){
			return ComboVARS.codigosiva_tipotaxa_reduzida;
		}else if(codigo.equals("INT")){
			return ComboVARS.codigosiva_tipotaxa_intermedia;
		}else if(codigo.equals("ISE")){
			return ComboVARS.codigosiva_tipotaxa_isenta;
		}

		return ComboVARS.codigosiva_tipotaxa_outras;

	}


	private int getEspacofiscal(String codigo) throws Exception {

		if(codigo.equals("PT")){
			return ComboVARS.codigosiva_espacofiscal_continente;
		}else if(codigo.equals("PT-MA")){
			return ComboVARS.codigosiva_espacofiscal_madeira;
		}else if(codigo.equals("PT-AC")){
			return ComboVARS.codigosiva_espacofiscal_acores;
		}

		return ComboVARS.codigosiva_espacofiscal_outros;

	}


	private void populateArtigos(MasterFiles masterFiles) throws Exception {

		for(Product product: masterFiles.getProductList()) {

			if(!ColibriDatabase.existsArtigos(product.getProductCode())){

				Artigos artigo=new Artigos(
						parse(product.getProductCode()),
						parse(product.getProductDescription()));
				artigo.addUnidades();
				artigo.addPrecos();
				artigo.setTipo(getTipoartigo(product.getProductType()));
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


	private int getTipoartigo(ProductType.Enum productType) throws Exception {

		switch(productType.intValue()){
		default: return ComboVARS.artigos_tipo_produto;
		case ProductType.INT_S: return ComboVARS.artigos_tipo_servico;
		case ProductType.INT_I: return ComboVARS.artigos_tipo_imposto;
		}

	}


	private void populateClientes(MasterFiles masterFiles) throws Exception {

		Integer numero=ColibriDatabase.getEntidades$NextNumero(tipoentidade.getCodigo());

		for(Customer customer: masterFiles.getCustomerList()) {

			Entidades entidade=new Entidades(tipoentidade, numero);
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





	/*
	 * Base de Dados
	 */
	private Codigospostais storeCodigospostais(String codigo, String descricao) throws Exception {

		if(!ColibriDatabase.existsCodigospostais(codigo)){
			Codigospostais codigopostal=new Codigospostais(parse(codigo),parse(descricao));
			Debug.out(codigopostal);
			ColibriDatabase.storeCodigospostais(codigopostal);
		}

		return ColibriDatabase.loadCodigospostais(codigo);

	}


	private Paises storePaises(String codigo, String descricao) throws Exception {

		if(!ColibriDatabase.existsPaises(codigo)){
			Paises pais=new Paises(parse(codigo),parse(descricao));
			Debug.out(pais);
			ColibriDatabase.storePaises(pais);
		}

		return ColibriDatabase.loadPaises(codigo);

	}


	private Familias storeFamilias(String codigo, String descricao) throws Exception {

		if(!ColibriDatabase.existsFamilias(codigo)){
			Familias familia=new Familias(parse(codigo),parse(descricao));
			Debug.out(familia);
			ColibriDatabase.storeFamilias(familia);
		}

		return ColibriDatabase.loadFamilias(codigo);

	}


	private Codigosiva storeCodigosiva(String codigo, String descricao, BigDecimal taxa, int tipotaxa, int espacofiscal) throws Exception {

		if(!ColibriDatabase.existsCodigosiva(codigo)){
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