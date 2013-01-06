/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package rcp.magento.dao.manager;

import java.util.List;

import rcp.magento.dao.MagentoDatabase;

import com.google.code.magja.model.category.Category;
import com.google.code.magja.service.RemoteServiceFactory;
import com.google.code.magja.service.category.CategoryRemoteService;

public class CategoryManager {

	private final CategoryRemoteService remoteService=RemoteServiceFactory.getCategoryRemoteService();

	public void saveCategory(Category category) {

		System.out.println(category);

		try {
			if (category.getId()==null){
				remoteService.save(category);
				System.out.println("CREATED");
			}else{
				remoteService.save(category);
				System.out.println("UPDATED");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("EXISTS");
		}
	}


	public void createCategory(String name) {

		System.out.println(name);

		try {
	        Category category = remoteService.getMinimalCategory(
	        		remoteService.getDefaultParent().getId(), name);

			saveCategory(category);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void listCategories() {

		try {
			Category categoryTree=remoteService.getByIdWithChildren(
				MagentoDatabase.getMagentoSoapClient().getConfig().getDefaultRootCategoryId());

			List<Category> categories=remoteService.getLastCategories(categoryTree);

			for (Category category : categories) {
			    System.out.println(category);
			}

			System.out.println(categories.size());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
