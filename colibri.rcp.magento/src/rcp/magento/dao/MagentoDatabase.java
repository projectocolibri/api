/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.dao;

import org.dma.utils.java.Debug;

import rcp.magento.dao.manager.CategoryManager;
import rcp.magento.dao.manager.CustomerManager;
import rcp.magento.dao.manager.ProductManager;

import com.google.code.magja.soap.MagentoSoapClient;
import com.google.code.magja.soap.SoapConfig;

public class MagentoDatabase {

	private static MagentoSoapClient magentoSoapClient;

	private static CategoryManager categoryManager;
	private static ProductManager productManager;
	private static CustomerManager customerManager;

	public static boolean initialize(SoapConfig soapConfig) {

		Debug.info();

		try{
			magentoSoapClient=MagentoSoapClient.getInstance(soapConfig);
			categoryManager=new CategoryManager();
			productManager=new ProductManager();
			customerManager=new CustomerManager();

			Debug.info("API USER", soapConfig.getApiUser());
			Debug.info("API KEY", soapConfig.getApiKey());
			Debug.info("REMOTE HOST", soapConfig.getRemoteHost());
			Debug.info("ATTRIBUTE SET ID", soapConfig.getDefaultAttributeSetId());
			Debug.info("ROOT CATEGORY ID", soapConfig.getDefaultRootCategoryId());

		}catch(Exception e){
			e.printStackTrace();
		}

		return magentoSoapClient!=null;

	}


	/*
	 * Client
	 */
	public static MagentoSoapClient getMagentoSoapClient() {
		return magentoSoapClient;
	}

	public static void setMagentoSoapClient(MagentoSoapClient magentoSoapClient) {
		MagentoDatabase.magentoSoapClient=magentoSoapClient;
	}


	/*
	 * Category
	 */
	public static CategoryManager getCategoryManager() {
		return categoryManager;
	}

	public static void setCategoryManager(CategoryManager categoryManager) {
		MagentoDatabase.categoryManager=categoryManager;
	}


	/*
	 * Product
	 */
	public static ProductManager getProductManager() {
		return productManager;
	}

	public static void setProductManager(ProductManager productManager) {
		MagentoDatabase.productManager=productManager;
	}


	/*
	 * Customer
	 */
	public static CustomerManager getCustomerManager() {
		return customerManager;
	}

	public static void setCustomerManager(CustomerManager customerManager) {
		MagentoDatabase.customerManager=customerManager;
	}


}
