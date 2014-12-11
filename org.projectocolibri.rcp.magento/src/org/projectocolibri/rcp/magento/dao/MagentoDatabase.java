/*******************************************************************************
 * 2008-2012 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.dao;

import com.google.code.magja.soap.MagentoSoapClient;
import com.google.code.magja.soap.SoapConfig;

import org.projectocolibri.rcp.magento.dao.manager.CategoryManager;
import org.projectocolibri.rcp.magento.dao.manager.CustomerManager;
import org.projectocolibri.rcp.magento.dao.manager.ProductManager;

public class MagentoDatabase {

	private static MagentoSoapClient magentoSoapClient;

	private static CategoryManager categoryManager;
	private static ProductManager productManager;
	private static CustomerManager customerManager;

	public static boolean initialize(SoapConfig soapConfig) {

		try{
			magentoSoapClient=MagentoSoapClient.getInstance(soapConfig);
			categoryManager=new CategoryManager();
			productManager=new ProductManager();
			customerManager=new CustomerManager();

			System.out.println("API USER: " + soapConfig.getApiUser());
			System.out.println("API KEY: " + soapConfig.getApiKey());
			System.out.println("REMOTE HOST: " + soapConfig.getRemoteHost());
			System.out.println("ATTRIBUTE SET ID: " + soapConfig.getDefaultAttributeSetId());
			System.out.println("ROOT CATEGORY ID: " + soapConfig.getDefaultRootCategoryId());

		}catch(Exception e){
			e.printStackTrace();
		}

		return magentoSoapClient!=null;

	}


	/*
	 * Managers
	 */
	public static MagentoSoapClient getMagentoSoapClient() {
		return magentoSoapClient;
	}

	public static CategoryManager getCategoryManager() {
		return categoryManager;
	}

	public static ProductManager getProductManager() {
		return productManager;
	}

	public static CustomerManager getCustomerManager() {
		return customerManager;
	}


}
