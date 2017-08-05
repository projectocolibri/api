/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.dao.manager;

import java.util.Collection;

import com.google.code.magja.model.customer.Customer;
import com.google.code.magja.service.RemoteServiceFactory;
import com.google.code.magja.service.customer.CustomerRemoteService;

import org.dma.java.util.StringUtils;

public class CustomerManager {

	private final CustomerRemoteService remoteService=RemoteServiceFactory.getSingleton().getCustomerRemoteService();

	public void saveCustomer(Customer customer) {

		System.out.println(customer);

		try{
			if (customer.getId()==null){
				remoteService.save(customer);
				System.out.println("CREATED");
			}else{
				remoteService.save(customer);
				System.out.println("UPDATED");
			}

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("EXISTS");
		}

	}


	public void createCustomer(String email) {

		System.out.println(email);

		try{
			Customer customer=new Customer();

			customer.setEmail(email);
			customer.setFirstName(StringUtils.capitalize(StringUtils.randomLetters(10)));
			customer.setLastName(StringUtils.capitalize(StringUtils.randomLetters(10)));
			customer.setPassword(StringUtils.randomLetters(8));
			customer.setPasswordHash(customer.getPassword());

			saveCustomer(customer);

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void listCustomers() {

		try{
			Collection<Customer> customers=remoteService.list();
			for (Customer customer : customers) {
				System.out.println(customer);
			}

			System.out.println(customers.size());

		}catch(Exception e){
			e.printStackTrace();
		}

	}


}
