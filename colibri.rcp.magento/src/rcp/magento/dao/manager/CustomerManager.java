/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.dao.manager;

import java.util.List;

import org.dma.java.utils.string.StringUtils;

import com.google.code.magja.model.customer.Customer;
import com.google.code.magja.service.RemoteServiceFactory;
import com.google.code.magja.service.customer.CustomerRemoteService;

public class CustomerManager {

	private final CustomerRemoteService remoteService=RemoteServiceFactory.getSingleton().getCustomerRemoteService();

	public void saveCustomer(Customer customer) {

		System.out.println(customer);

		try {
			if (customer.getId()==null){
				remoteService.save(customer);
				System.out.println("CREATED");
			}else{
				remoteService.save(customer);
				System.out.println("UPDATED");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("EXISTS");
		}

	}


	public void createCustomer(String email) {

		System.out.println(email);

		try {
			Customer customer=new Customer();

			customer.setEmail(email);
			customer.setFirstName(StringUtils.capitalize(StringUtils.random(10)));
			customer.setLastName(StringUtils.capitalize(StringUtils.random(10)));
			customer.setPassword(StringUtils.random(8));
			customer.setPasswordHash(customer.getPassword());

			saveCustomer(customer);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void listCustomers() {

		try {
			List<Customer> customers=remoteService.list();
			for (Customer customer : customers) {
				System.out.println(customer);
			}

			System.out.println(customers.size());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
