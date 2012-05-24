/*******************************************************************************
 * 2008-2012 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.importador.xml;

import java.io.File;
import java.math.BigDecimal;

import org.dma.utils.java.Debug;
import org.dma.utils.java.string.StringUtils;
import org.eclipse.swt.widgets.Label;

import rcp.colibri.core.vars.database.ComboVARS;
import rcp.colibri.core.vars.database.DefaultVARS;
import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.model.classes.Artigos;
import rcp.colibri.dao.model.classes.Codigosiva;
import rcp.colibri.dao.model.classes.Codigospostais;
import rcp.colibri.dao.model.classes.Empresa;
import rcp.colibri.dao.model.classes.Entidades;
import rcp.colibri.dao.model.classes.Entidadestipos;
import rcp.colibri.dao.model.classes.Familias;
import rcp.colibri.dao.model.classes.Paises;
import rcp.colibri.workbench.support.textinput.RegexSupport;
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

public class SaftPTImport {

	private final Label labelConsole;

	//Parametros
	private final boolean importHeader;
	private final boolean importCustomers;
	private final boolean importArticles;
	private final boolean importTaxes;
	private final Entidadestipos tipoentidade;

	//Documento principal
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
	private Header header;
	private MasterFiles masterFiles;

	public SaftPTImport(boolean importHeader, boolean importCustomers,
			boolean importArticles, boolean importTaxes, String tipoentidade,
			Label labelConsole) {

		this.importHeader = importHeader;
		this.importCustomers = importCustomers;
		this.importArticles = importArticles;
		this.importTaxes = importTaxes;
		this.tipoentidade = ColibriDatabase.loadEntidadestipos(tipoentidade);
		this.labelConsole = labelConsole;

	}


	public Boolean loadFile(File file){

		try{
			AuditFileDocument auditFileDocument = AuditFileDocument.Factory.parse(file);
			auditFile = auditFileDocument.getAuditFile();

			Debug.out("VALID", isValid());

			return isValid();

		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}


	public boolean process(){

		try{
			header = auditFile.getHeader();
			masterFiles = auditFile.getMasterFiles();

			if(importHeader)
				populateEmpresa();

			if(importCustomers)
				populateClientes();

			if(importArticles)
				populateArtigos();

			if(importTaxes)
				populateIVA();

			return true;

		}catch(Exception e){
			e.printStackTrace();
		}

		return false;
	}


	private Codigospostais createCodigopostal(String codigo, String descricao){

		if (!ColibriDatabase.existsCodigospostais(codigo)){
			Codigospostais codigopostal = new Codigospostais(filter(codigo),filter(descricao),"");
			Debug.out(codigopostal);
			ColibriDatabase.storeCodigospostais(codigopostal);
		}

		return ColibriDatabase.loadCodigospostais(codigo);

	}


	private Paises createPais(String codigo, String descricao){

		if (!ColibriDatabase.existsPaises(codigo)){
			Paises pais = new Paises(filter(codigo),filter(descricao));
			Debug.out(pais);
			ColibriDatabase.storePaises(pais);
		}

		return ColibriDatabase.loadPaises(codigo);

	}


	private Familias createFamilia(String codigo, String descricao){

		if (!ColibriDatabase.existsFamilias(codigo)){
			Familias familia = new Familias(filter(codigo),filter(descricao));
			Debug.out(familia);
			ColibriDatabase.storeFamilias(familia);
		}

		return ColibriDatabase.loadFamilias(codigo);

	}


	private Codigosiva createCodigoiva(String codigo, String descricao, BigDecimal taxa, int tipotaxa, int espacofiscal) {

		if (!ColibriDatabase.existsCodigosiva(codigo)){
			Codigosiva codigoiva = new Codigosiva(filter(codigo),filter(descricao),taxa,tipotaxa,espacofiscal);
			Debug.out(codigoiva);
			ColibriDatabase.storeCodigosiva(codigoiva);
		}

		return ColibriDatabase.loadCodigosiva(codigo);

	}


	private void populateEmpresa() throws Exception {

		Empresa empresa = ColibriDatabase.loadEmpresa(DefaultVARS.EMPRESA);

		//conservatoria e registo comercial
		if(header.getCompanyID().length()>0 && header.getCompanyID().contains(" ")){
			String[] companyId = header.getCompanyID().split(" ");
			empresa.setConservatoria(filter(companyId[0]));
			empresa.setRegistocomercial(filter(companyId[1]));
		}

		empresa.setNif(String.valueOf(header.getTaxRegistrationNumber()));
		empresa.setNome(filter(header.getCompanyName()));

		AddressStructurePT companyAddress = header.getCompanyAddress();
		empresa.setMorada(filter(companyAddress.getAddressDetail()));
		empresa.setLocalidade(filter(companyAddress.getCity()));
		empresa.setCodigopostal(createCodigopostal(companyAddress.getPostalCode(), companyAddress.getCity()));

		//TODO SAFT: Como sacar o PAIS da estrutura AddressStructurePT?
		/*
		empresa.setPais(createPais(
			((Country)companyAddress.getCountry()).getStringValue(),
			((Country)companyAddress.getCountry()).getStringValue()));
		*/

		empresa.setTelefone(filter(header.getTelephone()));
		empresa.setFax(filter(header.getFax()));
		empresa.setEmail(filter(header.getEmail()));
		empresa.setUrl(filter(header.getWebsite()));

		Debug.out(empresa);

		ColibriDatabase.storeEmpresa(empresa);
	}


	private void populateClientes() throws Exception {

		Customer[] customer = masterFiles.getCustomerArray();

		Integer numero=ColibriDatabase.getNextNumeroEntidades(tipoentidade.getCodigo());

		for (int i=0; i<customer.length; i++) {

			Entidades entidade = new Entidades(numero, tipoentidade);
			numero++;

			entidade.setNif(filter(customer[i].getCustomerTaxID()));
			entidade.setNome(filter(customer[i].getCompanyName()));
			entidade.setResponsavel(filter(customer[i].getContact()));

			AddressStructure addressStructure = customer[i].getBillingAddress();

			entidade.setMorada(filter(addressStructure.getAddressDetail()));
			entidade.setLocalidade(filter(addressStructure.getCity()));

			entidade.setCodigopostal(createCodigopostal(addressStructure.getPostalCode(), addressStructure.getCity()));
			entidade.setPais(createPais(addressStructure.getCountry(), addressStructure.getCountry()));

			entidade.setTelefone(filter(customer[i].getTelephone()));
			entidade.setFax(filter(customer[i].getFax()));
			entidade.setEmail(filter(customer[i].getEmail()));
			entidade.setUrl(filter(customer[i].getWebsite()));

			Debug.out(entidade);

			ColibriDatabase.storeEntidades(entidade);
			labelConsole.setText("Cliente: " + entidade.getNome().toUpperCase() + " gravado.");
		}
	}


	private void populateArtigos() throws Exception {

		Product[] product = masterFiles.getProductArray();

		for (int i=0; i<product.length; i++) {

			if(!ColibriDatabase.existsArtigos(product[i].getProductCode())){

				Artigos artigo = new Artigos(filter(product[i].getProductCode()));
				artigo.addUnidades(artigo.createUnidades());
				artigo.addPrecos(artigo.createPrecos());

				if(product[i].getProductType().equals("P")){
					artigo.setTipo(ComboVARS.artigos_tipo_produto);
				}else if(product[i].getProductType().equals("S")){
					artigo.setTipo(ComboVARS.artigos_tipo_servico);
				}else if(product[i].getProductType().equals("I")){
					artigo.setTipo(ComboVARS.artigos_tipo_imposto);
				}

				artigo.setDescricao(filter(product[i].getProductDescription()));
				artigo.setFamilia(createFamilia(product[i].getProductGroup(), product[i].getProductGroup()));

				//inicializa codigo de barras
				if(!product[i].getProductNumberCode().equals(product[i].getProductCode()))
					artigo.getUnidades().iterator().next().setCodigobarras(
						filter(product[i].getProductNumberCode()));

				Debug.out(artigo);

				ColibriDatabase.storeArtigos(artigo);
				labelConsole.setText("Artigo: " + artigo.getDescricao().toUpperCase() + " gravado.");
			}

		}
	}


	private void populateIVA() throws Exception {

		TaxTable[] taxTable = masterFiles.getTaxTableArray();

		for (int i=0; i<taxTable.length; i++) {

			TaxTableEntry[] taxTableEntry = taxTable[i].getTaxTableEntryArray();

			for (int j=0; j<taxTableEntry.length; j++) {

				createCodigoiva(
					filter(taxTableEntry[j].getTaxCode()),
					filter(taxTableEntry[j].getDescription()),
					taxTableEntry[j].getTaxPercentage(),
					getTaxCode(filter(taxTableEntry[j].getTaxCode())),
					getEspacoFiscal(filter(taxTableEntry[j].getTaxCountryRegion())));
			}
		}
	}


	private int getTaxCode(String code){

		int tipotaxa = ComboVARS.codigosiva_tipotaxa_outras;

		if(code.equals("Int") || code.equals("1"))
			tipotaxa = ComboVARS.codigosiva_tipotaxa_intermedia;
		else if(code.equals("Nor") || code.equals("2"))
			tipotaxa = ComboVARS.codigosiva_tipotaxa_normal;
		else if(code.equals("Red") || code.equals("3"))
			tipotaxa = ComboVARS.codigosiva_tipotaxa_reduzida;
		else if(code.equals("Ise") || code.equals("4"))
			tipotaxa = ComboVARS.codigosiva_tipotaxa_isenta;

		return tipotaxa;
	}


	private int getEspacoFiscal(String descricao){

		int espacofiscal = ComboVARS.codigosiva_espacofiscal_continente;

		if(descricao.equals("IVARAA")){
			espacofiscal = ComboVARS.codigosiva_espacofiscal_acores;
		}else if(descricao.equals("IVARAM")){
			espacofiscal = ComboVARS.codigosiva_espacofiscal_madeira;
		}

		return espacofiscal;
	}




	private String filter(String string){

		if(string==null)
			//evita strings a null
			string="";
		else
			//remove caracteres nao permitidos
			StringUtils.removeChars(string, RegexSupport.EXCLUDE_CHARS);

		return string;
	}





	/*
	 * Getters and setters
	 */
	public boolean isFileValid() {
		return auditFile!=null;
	}

	public boolean isValid() {
		return isFileValid() && auditFile.validate();
	}


}