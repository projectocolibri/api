/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento;

import org.dma.utils.java.Debug;
import org.dma.utils.java.numeric.IntegerUtils;
import org.dma.utils.java.string.StringUtils;

import rcp.magento.dao.MagentoDatabase;

import com.google.code.magja.soap.SoapConfig;

public class RCPMagento {

	public static final String MAGENTO_BASE_URL = "http://127.0.0.1/magento/";
	public static final String MAGENTO_ADMIN_URL = MAGENTO_BASE_URL+"index.php/admin/";
	public static final String MAGENTO_API_URL = MAGENTO_BASE_URL+"index.php/api/soap/";

	public static boolean login(){

		try{
			if (MagentoDatabase.getMagentoSoapClient()!=null){
				Debug.info("### MAGENTO LOGIN ###", "ALREADY LOGGED");
				return true;
			}
			/*
			 * Create a ROLE and USER with full access under
			 * Magento -> System -> Web Services
			 */
			if (MagentoDatabase.initialize(new SoapConfig("colibri7","colibri7",MAGENTO_API_URL))){
				Debug.info("### MAGENTO LOGIN ###", "OK!");
				return true;
			}

		} catch (Exception e){
			e.printStackTrace();
		}

		Debug.info("### MAGENTO LOGIN ###", "FAILED");

		return false;

	}


	public static boolean checkLogin(){

		if (MagentoDatabase.getMagentoSoapClient()!=null)
			return true;

		Debug.info("### PLEASE LOGIN ###");

		return false;

	}


	public static void listTest(){

		try{
			if (checkLogin()){

				// Pendente - http://code.google.com/p/magja/issues/detail?id=40
				//MagentoDatabase.getCategoryManager().listCategories();
				MagentoDatabase.getProductManager().listProducts();
				MagentoDatabase.getCustomerManager().listCustomers();

			}

		} catch (Exception e){
			e.printStackTrace();
		}
	}


	public static void createTest(){

		try{
			if (checkLogin()){

				// Pendente - http://code.google.com/p/magja/issues/detail?id=40
				//MagentoDatabase.getCategoryManager().createCategory(StringUtils.random(10));
				MagentoDatabase.getProductManager().createProduct("SKU_"+IntegerUtils.random(3));
				MagentoDatabase.getCustomerManager().createCustomer(
					StringUtils.random(10)+"@"+StringUtils.random(5)+".com");

			}

		} catch (Exception e){
			e.printStackTrace();
		}
	}


}
