/*******************************************************************************
 * 2011 Projecto Colibri
 * Sergio Gomes (sergiogomes@projectocolibri.com)
 *******************************************************************************/
package rcp.importador.xml;

import java.io.File;
import java.math.BigDecimal;

import org.dma.utils.java.Debug;
import org.dma.utils.java.string.StringUtils;
import org.eclipse.swt.widgets.Label;

import rcp.colibri.core.handlers.ExceptionHandler;
import rcp.colibri.core.vars.database.ComboVARS;
import rcp.colibri.dao.database.ColibriDatabase;
import rcp.colibri.dao.model.classes.Artigos;
import rcp.colibri.dao.model.classes.Codigosiva;
import rcp.colibri.dao.model.classes.Codigospostais;
import rcp.colibri.dao.model.classes.Entidades;
import rcp.colibri.dao.model.classes.Entidadestipos;
import rcp.colibri.dao.model.classes.Familias;
import rcp.colibri.dao.model.classes.Paises;
import rcp.colibri.workbench.support.textinput.RegexSupport;
import x0101.oecdStandardAuditFileTaxPT1.AddressStructure;
import x0101.oecdStandardAuditFileTaxPT1.AuditFileDocument;
import x0101.oecdStandardAuditFileTaxPT1.AuditFileDocument.AuditFile;
import x0101.oecdStandardAuditFileTaxPT1.AuditFileDocument.AuditFile.MasterFiles;
import x0101.oecdStandardAuditFileTaxPT1.CustomerDocument.Customer;
import x0101.oecdStandardAuditFileTaxPT1.ProductDocument.Product;
import x0101.oecdStandardAuditFileTaxPT1.TaxTableDocument.TaxTable;
import x0101.oecdStandardAuditFileTaxPT1.TaxTableEntryDocument.TaxTableEntry;

public class SaftPTImport {

	//Parametros
	private final boolean importHeader;
	private final boolean importCustomers;
	private final boolean importArticles;
	private final boolean importTaxes;
	private final Entidadestipos tipoentidade;

	//Documento principal
	private AuditFile auditFile;
	private MasterFiles masterFiles;

	Label labelConsole;

	public SaftPTImport(boolean importHeader, boolean importCustomers,
			boolean importArticles, boolean importTaxes, String tipoentidade) {

		this.importHeader = importHeader;
		this.importCustomers = importCustomers;
		this.importArticles = importArticles;
		this.importTaxes = importTaxes;
		this.tipoentidade = ColibriDatabase.loadEntidadestipos(tipoentidade);

	}


	public void loadFile(File file){

		Debug.info();
		try{
			if(file!=null){
				AuditFileDocument auditFileDocument = AuditFileDocument.Factory.parse(file);
				auditFile = auditFileDocument.getAuditFile();
			}
		}catch(Exception e){
			ExceptionHandler.error(e);
		}

	}


	public boolean process(){

		Debug.info();

		try{
			masterFiles = auditFile.getMasterFiles();

			if(importCustomers)
				populateClientes();

			if(importArticles)
				populateArtigos();

			if(importTaxes)
				populateIVA();

			return true;

		}catch(Exception e){
			ExceptionHandler.error(e);
		}

		return false;
	}


	private Codigospostais createCodigopostal(String codigo, String descricao){

		Debug.info("codigo", codigo);

		if (!ColibriDatabase.existsCodigospostais(codigo)){
			Codigospostais codigopostal = new Codigospostais(filter(codigo),filter(descricao),"");
			ColibriDatabase.storeCodigospostais(codigopostal);
		}

		return ColibriDatabase.loadCodigospostais(codigo);

	}


	private Paises createPais(String codigo, String descricao){

		Debug.info("codigo", codigo);

		if (!ColibriDatabase.existsPaises(codigo)){
			Paises pais = new Paises(filter(codigo),filter(descricao));
			ColibriDatabase.storePaises(pais);
		}

		return ColibriDatabase.loadPaises(codigo);

	}


	private Familias createFamilia(String codigo, String descricao){

		Debug.info("codigo", codigo);

		if (!ColibriDatabase.existsFamilias(codigo)){
			Familias familia = new Familias(filter(codigo),filter(descricao));
			ColibriDatabase.storeFamilias(familia);
		}

		return ColibriDatabase.loadFamilias(codigo);

	}


	private Codigosiva createCodigoiva(String codigo, String descricao, BigDecimal taxa, int tipotaxa, int espacofiscal) {

		Debug.info("codigo", codigo);

		if (!ColibriDatabase.existsCodigosiva(codigo)){
			Codigosiva codigoiva = new Codigosiva(filter(codigo),filter(descricao),taxa,tipotaxa,espacofiscal);
			ColibriDatabase.storeCodigosiva(codigoiva);
		}

		return ColibriDatabase.loadCodigosiva(codigo);

	}


	private void populateClientes() throws Exception {

		Customer[] customer = masterFiles.getCustomerArray();

		for (int i=0; i<customer.length; i++) {

			Entidades entidade = new Entidades(new Integer(customer[i].getCustomerID()), tipoentidade);

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

			ColibriDatabase.storeEntidades(entidade);
			labelConsole.setText("Costumer: " + entidade.getNome().toUpperCase() + " stored.");
		}
	}


	private void populateArtigos() throws Exception {

		Product[] product = masterFiles.getProductArray();

		for (int i=0; i<product.length; i++) {

			if(!ColibriDatabase.existsArtigos(product[i].getProductCode())){

				Artigos artigo = new Artigos(filter(product[i].getProductCode()));
				Debug.info("ARTIGO", artigo.getCodigo());

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

				ColibriDatabase.storeArtigos(artigo);
				labelConsole.setText("Article: " + artigo.getDescricao().toUpperCase() + " stored.");
			}

		}
	}


	private void populateIVA() throws Exception {

		Debug.info();

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
			string="";
		else
			StringUtils.removeChars(string, RegexSupport.EXCLUDE_CHARS);

		return string;
	}

	public void setLabelConsole(Label labelConsole){
		this.labelConsole =labelConsole;
	}


}